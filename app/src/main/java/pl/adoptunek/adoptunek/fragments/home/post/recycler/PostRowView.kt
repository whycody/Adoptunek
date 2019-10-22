package pl.adoptunek.adoptunek.fragments.home.post.recycler

import android.net.Uri

interface PostRowView {

    fun setShelterName(name: String)

    fun setTimeAgo(time: String)

    fun setShelterImage(uri: Uri)

    fun setPetImage(uri: Uri)

    fun setOnPetImageClickListener(id: String)

    fun setDataOfAnimals(data: List<Pair<String, String>>)
}