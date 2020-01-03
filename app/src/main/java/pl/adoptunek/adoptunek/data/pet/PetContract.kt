package pl.adoptunek.adoptunek.data.pet

import android.net.Uri
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post

interface PetContract {

    interface PetOfWeekInterractor{

        fun listWithWeekPetsIsReady(successfully: Boolean = true, postList: List<Post>? = null)
    }

    interface PetOfWeekListInterractor{

        fun listWithWeekPetsIDIsReady(successfully: Boolean = true, idList: List<String>? = null)
    }

    interface PetObjectInterractor{

        fun petDocumentIsReady(successfully: Boolean = true, pet: Pet? = null)
    }

    interface PetGalleryInterractor {

        fun photoIsReady(uri: Uri, index: Int)
    }

    interface PetDao{

        fun getPetsOfWeek()

        fun getPetsOfWeekIDList()

        fun getDocumentWithPet(id: String)

        fun getPhotosOfPet(id: String)
    }
}