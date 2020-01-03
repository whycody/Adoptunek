package pl.adoptunek.adoptunek

import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class Post(var idOfAnimal: String? = null,
                var idOfShelter: String? = null,
                var petName: String? = null,
                var shelterName: String? = null,
                var timeAgo: String? = null,
                var shelterUri: String? = null,
                var petUri: String? = null,
                var petOfWeek: Boolean? = false,
                var dataOfAnimal: List<Pair<String, String>>? = null,
                var pet: Pet? = null): Serializable

data class Pet(val name: String? = null,
               val shelter: String? = null,
               val sex: String? = null,
               val breed: String? = null,
               var id: String? = null,
               var describe: String? = null,
               var coat: String? = null,
               var siblings: String? = null,
               var full_health: String? = null,
               var character: String? = null,
               var attitude_to_people: String? = null,
               var pet_of_week: Boolean? = null,
               var profile_image_uri: String? = null,
               @ServerTimestamp var add_date: Date? = null,
               @ServerTimestamp var birth_date: Date? = null,
               @ServerTimestamp var in_shelter: Date? = null,
               val type: String? = null): Serializable

data class PetOfWeek(val pets: List<String>? = null)

data class Shelter(var name: String? = null,
                   var id: String? = null,
                   var location_latitude: Double? = null,
                   var location_longitude: Double? = null)