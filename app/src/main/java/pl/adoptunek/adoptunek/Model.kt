package pl.adoptunek.adoptunek

import android.net.Uri
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Post(var idOfAnimal: String = "",
                var idOfShelter: String = "",
                var shelterName: String = "",
                var timeAgo: String = "",
                var shelterUri: Uri = Uri.EMPTY,
                var petUri: Uri = Uri.EMPTY)

data class Pet(val name: String = "",
               val shelter: String = "",
               @ServerTimestamp var add_date: Date? = null,
               val type: String = "")

data class Shelter(val name: String = "")