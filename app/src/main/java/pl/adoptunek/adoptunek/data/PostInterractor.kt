package pl.adoptunek.adoptunek.data

import pl.adoptunek.adoptunek.Post

interface PostInterractor {

    fun listIsReady(list: List<Post>)
}