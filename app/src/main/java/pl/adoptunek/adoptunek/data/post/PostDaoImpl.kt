package pl.adoptunek.adoptunek.data.post

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.Shelter
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelper
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PostDaoImpl(private val interractor: PostInterractor? = null): PostDao {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val postList = mutableListOf<Post>()
    private val timeHelper: TimeHelper = TimeHelperImpl()
    private var lastSnapshot: DocumentSnapshot? = null
    private var countOfPosts = 4
    private var limitOfPosts = 4L

    private fun getPostOfPetObject(id: String){
        firestore.collection("animals").document(id).get().addOnCompleteListener{ task ->
            if(task.isSuccessful) {
                val newPost = Post()
                val pet = task.result!!.toObject(Pet::class.java)
                loadPetInfoToPostObject(newPost, pet!!)
                //TODO get Uri of pet and shelter and data with basic informations
            }
        }
    }

    override fun getPosts(reset: Boolean){
        if(reset) postList.clear()
        val firstQuery = firestore.collection("animals")
            .orderBy("add_date", Query.Direction.DESCENDING).limit(limitOfPosts)
        firstQuery.get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                lastSnapshot = task.result!!.documents.get(task.result!!.size()-1)
                countOfPosts = task.result!!.size()
                for(document in task.result!!){
                    val newPost = Post()
                    val pet = document.toObject(Pet::class.java)
                    newPost.idOfAnimal = document.id
                    loadPetInfoToPostObject(newPost, pet)
                    getShelter(pet, newPost)
                }
            }
        }.addOnFailureListener{ Log.d(TAG, "Failure getPosts")}
    }

    override fun loadMorePosts() {
        val secondQuery = firestore.collection("animals")
            .orderBy("add_date", Query.Direction.DESCENDING)
            .startAfter(lastSnapshot!!)
            .limit(limitOfPosts)
        secondQuery.get().addOnCompleteListener{ task ->
            if(task.isSuccessful && task.result!!.size()>0){
                lastSnapshot = task.result!!.documents.get(task.result!!.size()-1)
                countOfPosts += task.result!!.size()
                if(task.result!!.size()<limitOfPosts) interractor?.endListOfPosts()
                for(document in task.result!!){
                    val newPost = Post()
                    val pet = document.toObject(Pet::class.java)
                    newPost.idOfAnimal = document.id
                    loadPetInfoToPostObject(newPost, pet)
                    getShelter(pet, newPost)
                }
            }else interractor?.endListOfPosts()
        }.addOnFailureListener{ Log.d(TAG, "Failure getPosts")}
    }

    private fun loadPetInfoToPostObject(post: Post, pet: Pet){
        post.idOfShelter = pet.shelter
        post.addDate = pet.add_date
        post.petName = pet.name
        post.dataOfAnimal = null
        post.dataOfAnimal = getBasicDataOfPet(pet)
        post.timeAgo = timeHelper.howLongAgo(pet.add_date!!,
            TimeHelperImpl.POST_HOW_LONG_AGO)
    }

    private fun getBasicDataOfPet(pet: Pet): List<Pair<String, String>>{
        val dataOfAnimalList = mutableListOf<Pair<String, String>>()
        if(pet.name!=null) dataOfAnimalList.add(Pair("Imię", pet.name))
        if(pet.sex!=null) dataOfAnimalList.add(Pair("Płeć", pet.sex))
        if(pet.birth_date!=null) dataOfAnimalList.add(Pair("Wiek",
            timeHelper.howLongAgo(pet.birth_date!!, TimeHelperImpl.PET_HOW_LONG_IS_WAITING)))
        return dataOfAnimalList
    }

    private fun getShelter(pet: Pet, post: Post){
        firestore.collection("shelters").document(pet.shelter!!).get()
            .addOnCompleteListener{ res ->
                if(res.isSuccessful){
                    val shelter = res.result!!.toObject(Shelter::class.java)
                    post.shelterName = "Schronisko \"${shelter!!.name}\""
                }
                gotShelterInListOfPosts(post, res.isSuccessful)
            }
    }

    private fun gotShelterInListOfPosts(post: Post, successfull: Boolean){
        if(successfull) postList.add(post)
        else countOfPosts--
        if(postList.size>=limitOfPosts) listIsReady()
    }

    override fun getPetUri(holder: PostViewHolder?, post: Post){
        val petPath = "animals_photos/${post.idOfAnimal}/profile.jpg"
        storageReference.child(petPath).downloadUrl.addOnSuccessListener { petImage ->
            holder?.setPetImage(petImage)
            post.petUri = petImage.toString()
        }
    }

    override fun getShelterUri(holder: PostViewHolder?, post: Post){
        val shelterPath = "shelter/${post.idOfShelter}/profile.png"
        storageReference.child(shelterPath).downloadUrl.addOnSuccessListener { shelterImage ->
            holder?.setShelterImage(shelterImage)
            post.shelterUri = shelterImage.toString()
        }.addOnFailureListener{
            getDefaultShelterUri(holder, post)
        }
    }

    private fun getDefaultShelterUri(holder: PostViewHolder?, post: Post){
        val defaultShelterPath = "shelter/default.png"
        storageReference.child(defaultShelterPath).downloadUrl.addOnSuccessListener { shelterImage ->
            holder?.setShelterImage(shelterImage)
            post.shelterUri = shelterImage.toString()
        }
    }

    private fun listIsReady(){
        val sortedList = postList
        if(sortedList.size==limitOfPosts.toInt()) interractor?.listOfPostsIsReady(sortedList)
        else interractor?.listOfPostsIsUpdated(sortedList, sortedList.size==countOfPosts)
    }

    companion object{
        val TAG = "PostDaoImplTAG"
    }
}