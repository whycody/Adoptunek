package pl.adoptunek.adoptunek.data.post

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.Shelter
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelper
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PostDaoImpl(private val interractor: PostInterractor): PostDao {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val postList = mutableListOf<Post>()
    private val timeHelper: TimeHelper = TimeHelperImpl()
    private var countOfPosts = 3

    override fun getPosts(count: Int){
        val firstQuery = firestore.collection("animals")
            .orderBy("add_date", Query.Direction.DESCENDING)
        firstQuery.get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                for(document in task.result!!){
                    countOfPosts = task.result!!.size()
                    val newPost = Post()
                    val idOfAnimal = document.id
                    val pet = document.toObject(Pet::class.java)
                    newPost.addDate = (document.get("add_date") as Timestamp).toDate()
                    newPost.petName = pet.name
                    newPost.idOfAnimal = idOfAnimal
                    newPost.dataOfAnimal = getDataOfAnimalList(pet)
                    newPost.timeAgo = timeHelper.howLongAgo(pet.add_date!!,
                        TimeHelperImpl.POST_HOW_LONG_AGO)
                    firestore.collection("shelters").document(pet.shelter!!).get()
                        .addOnSuccessListener { getShelter(idOfAnimal, pet, newPost) }
                }
            }
        }.addOnFailureListener{ Log.d(TAG, "Failure getPosts")}
    }

    private fun getDataOfAnimalList(pet: Pet): List<Pair<String, String>>{
        val dataOfAnimalList = mutableListOf<Pair<String, String>>()
        if(pet.name!=null) dataOfAnimalList.add(Pair("Imię", pet.name))
        if(pet.sex!=null) dataOfAnimalList.add(Pair("Płeć", pet.sex))
        if(pet.birth_date!=null) dataOfAnimalList.add(Pair("Wiek", timeHelper.howLongAgo(pet.birth_date!!,
            TimeHelperImpl.PET_HOW_LONG_IS_WAITING)))
        return dataOfAnimalList
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
            post.petUri = petImage.toString()
            getShelterUri(pet, post)
        }.addOnFailureListener{ Log.d(TAG, "Failure getPetUri")}
    }

    private fun getShelterUri(pet: Pet, post: Post){
        val shelterPath = "shelter/${pet.shelter}/profile.png"
        post.idOfShelter = pet.shelter
        storageReference.child(shelterPath).downloadUrl.addOnSuccessListener { shelterImage ->
            post.shelterUri = shelterImage.toString()
            postList.add(post)
            if(postList.size==countOfPosts) listIsReady()
        }.addOnFailureListener{
            getDefaultShelterUri(post)
        }
    }

    private fun getDefaultShelterUri(post: Post){
        val defaultShelterPath = "shelter/default.png"
        storageReference.child(defaultShelterPath).downloadUrl.addOnSuccessListener { shelterImage ->
            post.shelterUri = shelterImage.toString()
            postList.add(post)
            if(postList.size==countOfPosts) listIsReady()
        }.addOnFailureListener{ Log.d(TAG, "Failure getDefaultShelter")}
    }

    private fun listIsReady(){
        val sortedList = postList.sortedWith(compareBy { it.addDate }).reversed()
        interractor.listOfPostsIsReady(sortedList)
    }

    companion object{
        val TAG = "PostDaoImplTAG"
    }
}