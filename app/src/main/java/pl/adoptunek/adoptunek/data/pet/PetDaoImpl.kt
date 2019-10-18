package pl.adoptunek.adoptunek.data.pet

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet

class PetDaoImpl(val interractor: PetContract.PetInterractor): PetContract.PetDao {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val petList = mutableListOf<Pet>()

    override fun getPetsOfWeek() {
        val collection = firestore.collection("animals")
        collection.whereEqualTo("pet_of_week", true).limit(3).get().addOnSuccessListener{ documents ->
            for(document in documents){
                val pet = document.toObject(Pet::class.java)
                pet.id = document.id
                getPetImage(pet)
            }
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