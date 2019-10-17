package pl.adoptunek.adoptunek.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.Shelter
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelper
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PostDaoImpl(val interractor: PostInterractor): PostDao{

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val postList = mutableListOf<Post>()
    private val timeHelper: TimeHelper = TimeHelperImpl()
    private var countOfPosts = 3

    override fun getPosts(count: Int){
        val collection = firestore.collection("animals")
        collection.limit(count.toLong()).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                for(document in task.result!!){
                    countOfPosts = task.result!!.size()
                    val newPost = Post()
                    val idOfAnimal = document.id
                    newPost.idOfAnimal = idOfAnimal
                    val pet = document.toObject(Pet::class.java)
                    val dataOfAnimalList = mutableListOf<Pair<String, String>>()
                    if(pet.name!=null) dataOfAnimalList.add(Pair("Imię", pet.name))
                    if(pet.sex!=null) dataOfAnimalList.add(Pair("Płeć", pet.sex))
                    if(pet.in_shelter!=null) dataOfAnimalList.add(Pair("Czeka", timeHelper.howLongIsWaiting(pet.in_shelter!!)))
                    newPost.dataOfAnimal = dataOfAnimalList
                    newPost.timeAgo = timeHelper.howLongAgo(pet.add_date!!)
                    firestore.collection("shelters").document(pet.shelter!!).get()
                        .addOnSuccessListener { getShelter(idOfAnimal, pet, newPost) }
                }
            }
        }.addOnFailureListener{ Log.d(TAG, "Failure getPosts")}
    }

    private fun getShelter(id: String, pet: Pet, post: Post){
        firestore.collection("shelters").document(pet.shelter!!).get()
            .addOnSuccessListener { doc ->
                val shelter = doc.toObject(Shelter::class.java)
                post.shelterName = "Schronisko \"${shelter!!.name}\""
                getPetUri(id, pet, post)
            }.addOnFailureListener{ Log.d(TAG, "Failure getShelter")}
    }

    private fun getPetUri(id: String, pet: Pet, post: Post){
        val petPath = "animals_photos/${id}/profile.jpg"
        storageReference.child(petPath).downloadUrl.addOnSuccessListener { petImage ->
            post.petUri = petImage
            getShelterUri(pet, post)
        }.addOnFailureListener{ Log.d(TAG, "Failure getPetUri")}
    }

    private fun getShelterUri(pet: Pet, post: Post){
        val shelterPath = "shelter/${pet.shelter}/profile.png"
        post.idOfShelter = pet.shelter
        storageReference.child(shelterPath).downloadUrl.addOnSuccessListener { shelterImage ->
            post.shelterUri = shelterImage
            postList.add(post)
            if(postList.size==countOfPosts) interractor.listIsReady(postList)
        }.addOnFailureListener{
            getDefaultShelterUri(post)
        }
    }

    private fun getDefaultShelterUri(post: Post){
        val defaultShelterPath = "shelter/default.png"
        storageReference.child(defaultShelterPath).downloadUrl.addOnSuccessListener { shelterImage ->
            post.shelterUri = shelterImage
            postList.add(post)
            if(postList.size==countOfPosts) interractor.listIsReady(postList)
        }.addOnFailureListener{ Log.d(TAG, "Failure getDefaultShelter")}
    }

    companion object{
        val TAG = "PostDaoImplTAG"
    }
}