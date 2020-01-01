package pl.adoptunek.adoptunek.data.converter

import pl.adoptunek.adoptunek.Post

interface PetConverterInterractor {

    fun convertedIntoPostObject(post: Post?, successfully: Boolean = true)
}