package pl.adoptunek.adoptunek.data

import pl.adoptunek.adoptunek.Post

interface PostDao {

    fun getPosts(count: Int)
}