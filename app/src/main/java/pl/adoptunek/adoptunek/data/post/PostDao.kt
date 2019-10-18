package pl.adoptunek.adoptunek.data.post

import pl.adoptunek.adoptunek.Post

interface PostDao {

    fun getPosts(count: Int)
}