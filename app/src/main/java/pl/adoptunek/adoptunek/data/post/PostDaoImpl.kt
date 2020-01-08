package pl.adoptunek.adoptunek.data.post

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.Shelter
import pl.adoptunek.adoptunek.data.converter.PetConverterImpl
import pl.adoptunek.adoptunek.data.pet.PetContract
import pl.adoptunek.adoptunek.data.pet.PetDaoImpl
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelper
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PostDaoImpl(private val interractor: PostInterractor? = null):
    PostDao, PetContract.PetOfWeekListInterractor {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val petConverter = PetConverterImpl()
    private val postList = mutableListOf<Post>()
    private val petDao = PetDaoImpl(petOfWeekListInterractor = this)
    private val timeHelper: TimeHelper = TimeHelperImpl()
    private var lastSnapshot: DocumentSnapshot? = null
    private var countOfPosts = 3
    private var limitOfPosts = 3L
    private var petOfWeekIDList: List<String>? = null
    private var reset = false

    override fun getPosts(reset: Boolean){
        this.reset = reset
        petDao.getPetsOfWeekIDList()
    }

    override fun listWithWeekPetsIDIsReady(successfully: Boolean, idList: List<String>?) {
        petOfWeekIDList = idList
        generatePosts()
    }

    private fun generatePosts(){
        if(reset) postList.clear()
        val firstQuery = firestore.collection("animals")
            .orderBy("add_date", Query.Direction.DESCENDING)
            .limit(limitOfPosts)
        firstQuery.get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                lastSnapshot = task.result!!.documents.get(task.result!!.size()-1)
                countOfPosts = task.result!!.size()
                for(document in task.result!!) generateNewPost(document)
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
                lastSnapshot = task.result!!.documents[task.result!!.size()-1]
                countOfPosts += task.result!!.size()
                if(task.result!!.size()<limitOfPosts) interractor?.endListOfPosts()
                for(document in task.result!!) generateNewPost(document)
            }else interractor?.endListOfPosts()
        }.addOnFailureListener{ Log.d(TAG, "Failure getPosts")}
    }

    private fun generateNewPost(document: QueryDocumentSnapshot) {
        val newPost = Post()
        val pet = document.toObject(Pet::class.java)
        newPost.idOfAnimal = document.id
        loadPetInfoToPostObject(newPost, pet)
        addPetOfWeekInformation(newPost)
        getShelter(pet, newPost)
    }

    private fun loadPetInfoToPostObject(post: Post, pet: Pet){
        post.pet = pet
        post.idOfShelter = pet.shelter
        post.petName = pet.name
        post.dataOfAnimal = null
        post.timeAgo = timeHelper.howLongAgo(pet.add_date!!,
            TimeHelperImpl.POST_HOW_LONG_AGO)
        petConverter.addBasicDataToPost(post)
    }

    private fun addPetOfWeekInformation(post: Post){
        if(petOfWeekIDList!=null){
            if(petOfWeekIDList!!.contains(post.idOfAnimal))
                post.petOfWeek = true
        }
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
        val DEFAULT_SHELTER_PATH = "shelter/default.png"
    }
}