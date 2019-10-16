package pl.adoptunek.adoptunek.data

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.Shelter

class PostDaoImpl(val interractor: PostInterractor): PostDao{

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun getPosts(count: Int){
        var postList = mutableListOf<Post>()
        val collection = firestore.collection("animals")
        collection.limit(count.toLong()).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                for(document in task.result!!){
                    val id = document.id
                    val pet = document.toObject(Pet::class.java)
                    firestore.collection("shelters").document(pet.shelter).get()
                        .addOnSuccessListener { doc ->
                            val shelter = doc.toObject(Shelter::class.java)
                            val nameOfShelter = "Schronisko \"${shelter!!.name}\""
                            val storageReference = storage.reference
                            val petPath = "animals_photos/${id}/profile.jpg"
                            val shelterPath = "shelter/${pet.shelter}/profile.png"
                            storageReference.child(petPath).downloadUrl.addOnSuccessListener { petImage ->
                                storageReference.child(shelterPath).downloadUrl.addOnSuccessListener{ shelterImage ->
                                    postList.add(Post(nameOfShelter, "2 godz. temu", shelterImage, petImage))
                                    if(postList.size==(task.result!!.size())) interractor.listIsReady(postList)
                                }.addOnFailureListener{
                                    Log.d("MOJTAG", "Nie udalo sie 2 ${shelterPath}")
                                }
                            }.addOnFailureListener{
                                Log.d("MOJTAG", "Nie udalo sie 1")
                            }
                    }

                }
            }
        }
//        val petList = document.limit(count.toLong()).get().addOnSuccessListener { querySnapshot ->
//            for(document in querySnapshot.documents){
//                val id = document.id
//                val pet = document as Pet
//                val shelter = firestore.collection("shelters").document(pet.shelter).get(Shelter::class.java)
//                val shelterName = shelter.name
//                val timeAgo = "2 godz. temu"
//                val storageReference = storage.reference
//                val petPath = "animal_photos/${id}/profile.pl"
//                val shelterPath = "shelter/${pet.shelter}/profile.png"
//                val petImage = storageReference.child(petPath).downloadUrl
//
//            }
//        }
        val post = Post("Schronisko \"Nowy dom\"", "2 godz. temu",
            Uri.parse("https://firebasestorage.googleapis.com/v0/b/adoptunek.appspot.com/o/shelter%2Fprofile.png?alt=media&token=747baf77-d8cb-41d9-bf99-1d7599d22203")
        ,Uri.parse("https://firebasestorage.googleapis.com/v0/b/adoptunek.appspot.com/o/animals_photos%2FJ5uJ878MNExQ7017WUYl%2Fprofile.jpg?alt=media&token=4acb1f50-a8c5-4602-89d2-531edbbf0c44"))
        val list = List(1){post}
    }
}