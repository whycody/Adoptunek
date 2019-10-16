package pl.adoptunek.adoptunek

import android.net.Uri

data class Post(val shelterName: String, val timeAgo: String, val shelterUri: Uri, val petUri: Uri)
data class Pet(val name: String = "", val shelter: String = "", val type: String = "")
data class Shelter(val name: String = "")