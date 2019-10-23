package pl.adoptunek.adoptunek.data.pet

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.PetOfWeek

class PetDaoImpl(val petObjectsInterractor: PetContract.PetObjectsInterractor? = null,
                 val petGalleryInterractor: PetContract.PetGalleryInterractor? = null): PetContract.PetDao {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val petList = mutableListOf<Pet>()

    override fun getPetsOfWeek() {
        val petOfWeekCollection = firestore.collection("pet_of_week")
        petOfWeekCollection.document("21102019").get().addOnSuccessListener{ document ->
            val petOfWeek = document.toObject(PetOfWeek::class.java)
            for(id in petOfWeek!!.pets!!) getDocumentWithPet(id, true)
        }
    }

    override fun getDocumentWithPet(id: String, collection: Boolean){
        val animalsCollection = firestore.collection("animals")
        animalsCollection.document(id).get().addOnSuccessListener{ document ->
            val pet = document.toObject(Pet::class.java)
            pet!!.id = id
            if(collection) getPetImage(pet)
            else petObjectsInterractor?.petDocumentIsReady(true, pet)
        }.addOnFailureListener{
            petObjectsInterractor?.listWithPetsIsReady(false)
        }
    }

    private fun getPetImage(pet: Pet){
        val petPath = "animals_photos/${pet.id}/profile.jpg"
        storageRef.child(petPath).downloadUrl.addOnSuccessListener{ uri ->
            pet.profile_image_uri = uri.toString()
            petList.add(pet)
            if(petList.size==3) petObjectsInterractor?.listWithPetsIsReady(true, petList)
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

}