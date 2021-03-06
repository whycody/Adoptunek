package pl.adoptunek.adoptunek.data.pet

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.PetOfWeek
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.data.converter.PetConverterImpl
import pl.adoptunek.adoptunek.data.converter.PetConverterInterractor

class PetDaoImpl(val petGalleryInterractor: PetContract.PetGalleryInterractor? = null,
                 val petOfWeekInterractor: PetContract.PetOfWeekInterractor? = null,
                 val petObjectInterractor: PetContract.PetObjectInterractor? = null,
                 val petOfWeekListInterractor: PetContract.PetOfWeekListInterractor? = null):
    PetContract.PetDao, PetConverterInterractor {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val postList = mutableListOf<Post>()
    private val petConverter = PetConverterImpl(this)
    private val petOfWeekCollection = firestore.collection("pet_of_week")

    override fun getPetsOfWeek() {
        petOfWeekCollection.document(getCurrentWeek()).get().addOnSuccessListener{ document ->
            val petOfWeek = document.toObject(PetOfWeek::class.java)
            for(id in petOfWeek!!.pets!!) petConverter.getPostFromPetID(id, false, true)
        }
    }

    override fun getPetsOfWeekIDList() {
        val idList = mutableListOf<String>()
        petOfWeekCollection.document(getCurrentWeek()).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val petOfWeek = task.result!!.toObject(PetOfWeek::class.java)
                for(id in petOfWeek!!.pets!!) idList.add(id)
            }
            petOfWeekListInterractor?.listWithWeekPetsIDIsReady(task.isSuccessful, idList)
        }
    }

    private fun getCurrentWeek(): String{
        //TODO get current week in this function
        return "21102019"
    }

    override fun getDocumentWithPet(id: String){
        val animalsCollection = firestore.collection("animals")
        animalsCollection.document(id).get().addOnSuccessListener{ document ->
            val pet = document.toObject(Pet::class.java)
            pet!!.id = id
            petObjectInterractor?.petDocumentIsReady(true, pet)
        }
    }

    private val photosOfPet = mutableListOf<Uri>()
    private var numberOfPhotos: Int? = null

    override fun getPhotosOfPet(id: String) {
        val petsPath = "animals_photos/${id}/"
        storageRef.child(petsPath).listAll().addOnSuccessListener{ listResult ->
            val items = listResult.items
            numberOfPhotos = items.size
            getUrisFromPhotos(items.size-1, items)
        }
    }

    private fun getUrisFromPhotos(index: Int = 0, items: List<StorageReference>) {
        items[index].downloadUrl.addOnSuccessListener { uri ->
            if (numberOfPhotos != null) {
                petGalleryInterractor?.photoIsReady(uri, photosOfPet.size)
                photosOfPet.add(uri)
                if(numberOfPhotos!=photosOfPet.size) getUrisFromPhotos(index - 1, items)
            }
        }
    }

    override fun convertedIntoPostObject(post: Post?, successfully: Boolean) {
        if(successfully){
            post!!.petOfWeek = true
            postList.add(post)
        }
        if(postList.size==3) petOfWeekInterractor?.listWithWeekPetsIsReady(true, postList)
    }
}