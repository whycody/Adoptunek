package pl.adoptunek.adoptunek.data.pet

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.PetOfWeek

class PetDaoImpl(val interractor: PetContract.PetInterractor): PetContract.PetDao {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val petList = mutableListOf<Pet>()

    override fun getPetsOfWeek() {
        val petOfWeekCollection = firestore.collection("pet_of_week")
        petOfWeekCollection.document("21102019").get().addOnSuccessListener{ document ->
            val petOfWeek = document.toObject(PetOfWeek::class.java)
            for(id in petOfWeek!!.pets!!) getDocumentWithPet(id)
        }
    }

    private fun getDocumentWithPet(id: String){
        val animalsCollection = firestore.collection("animals")
        animalsCollection.document(id).get().addOnSuccessListener{ document ->
            val pet = document.toObject(Pet::class.java)
            pet!!.id = id
            getPetImage(pet)
        }.addOnFailureListener{
            interractor.listWithPetsIsReady(false)
        }
    }

    private fun getPetImage(pet: Pet){
        val petPath = "animals_photos/${pet.id}/profile.jpg"
        storageRef.child(petPath).downloadUrl.addOnSuccessListener{ uri ->
            pet.profile_image = uri
            petList.add(pet)
            if(petList.size==3) interractor.listWithPetsIsReady(true, petList)
        }
    }


}