package pl.adoptunek.adoptunek.data.offline

import com.google.firebase.firestore.*

class OfflineDatabaseImpl: OfflineDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val animalCollection = "animals"
    private val petOfWeekCollection = "pet_of_week"
    private val sheltersCollection = "shelters"

    override fun initializeOfflineDatabase() {
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        firestore.firestoreSettings = settings
        firestore.collection(animalCollection).addSnapshotListener(MetadataChanges.INCLUDE){
            querySnapshot, e -> if(e!=null) return@addSnapshotListener
        }
        firestore.collection(petOfWeekCollection).addSnapshotListener(MetadataChanges.INCLUDE){
                querySnapshot, e -> if(e!=null) return@addSnapshotListener
        }
        firestore.collection(sheltersCollection).addSnapshotListener(MetadataChanges.INCLUDE){
                querySnapshot, e -> if(e!=null) return@addSnapshotListener
        }
    }
}