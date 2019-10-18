package pl.adoptunek.adoptunek.data.post

import pl.adoptunek.adoptunek.Post

interface PostInterractor {

    fun listOfPostsIsReady(list: List<Post>)
}