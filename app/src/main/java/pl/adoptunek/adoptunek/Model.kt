package pl.adoptunek.adoptunek

import android.net.Uri
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Post(var idOfAnimal: String? = null,
                var idOfShelter: String? = null,
                var shelterName: String? = null,
                var timeAgo: String? = null,
                var shelterUri: Uri? = null,
                var petUri: Uri? = null,
                var dataOfAnimal: List<Pair<String, String>>? = null)

data class Pet(val name: String? = null,
               val shelter: String? = null,
               val sex: String? = null,
               @ServerTimestamp var add_date: Date? = null,
               @ServerTimestamp var in_shelter: Date? = null,
               val type: String? = null)

data class Shelter(val name: String = "")