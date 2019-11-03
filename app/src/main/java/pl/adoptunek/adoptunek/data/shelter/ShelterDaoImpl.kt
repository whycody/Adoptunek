package pl.adoptunek.adoptunek.data.shelter

import com.google.firebase.firestore.FirebaseFirestore
import pl.adoptunek.adoptunek.Shelter

class ShelterDaoImpl(val interractor: ShelterContract.ShelterInterractor): ShelterContract.ShelterDao {

    private val firestore = FirebaseFirestore.getInstance()
    private val sheltersList = mutableListOf<Shelter>()

    override fun getAllShelters() {
        val shelterCollection = firestore.collection("shelters")
        shelterCollection.get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                for(document in task.result!!){
                    val shelter = document.toObject(Shelter::class.java)
                    shelter.id = document.id
                    sheltersList.add(shelter)
                    interractor.shelterIsReady(shelter = shelter)
                    if(sheltersList.size==task.result!!.size())
                        interractor.listWithSheltersIsReady(true, sheltersList)
                }
            }else interractor.listWithSheltersIsReady(false)
        }
    }

}