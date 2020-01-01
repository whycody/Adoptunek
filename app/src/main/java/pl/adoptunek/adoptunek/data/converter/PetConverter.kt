package pl.adoptunek.adoptunek.data.converter

import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post

interface PetConverter {

    fun getPostFromPetID(id: String, fullData: Boolean, withPhotos: Boolean)

    fun getPostFromPetObject(pet: Pet, fullData: Boolean, withPhotos: Boolean)

    fun addBasicDataToPost(post: Post)

    fun addFullDataToPost(post: Post)
}