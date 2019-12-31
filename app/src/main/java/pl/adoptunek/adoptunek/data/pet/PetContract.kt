package pl.adoptunek.adoptunek.data.pet

import android.net.Uri
import pl.adoptunek.adoptunek.Pet

interface PetContract {

    interface PetOfWeekInterractor{

        fun listWithWeekPetsIsReady(successfully: Boolean = true, petList: List<Pet>? = null)
    }

    interface PetObjectInterractor{

        fun petDocumentIsReady(successfully: Boolean = true, pet: Pet? = null)
    }

    interface PetGalleryInterractor {

        fun photoIsReady(uri: Uri, index: Int)

        fun listWithPhotosIsReady(successfully: Boolean = true, list: List<Uri>)
    }

    interface PetDao{

        fun getPetsOfWeek()

        fun getDocumentWithPet(id: String, collection: Boolean = false)

        fun getPhotosOfPet(id: String)
    }
}