package pl.adoptunek.adoptunek.data.post

import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder

interface PostDao {

    fun getPosts(reset: Boolean = false)

    fun loadMorePosts()

    fun getPetUri(holder: PostViewHolder, post: Post)

    fun getShelterUri(holder: PostViewHolder, post: Post)
}