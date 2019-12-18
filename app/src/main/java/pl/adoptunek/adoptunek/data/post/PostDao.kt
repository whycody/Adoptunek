package pl.adoptunek.adoptunek.data.post

interface PostDao {

    fun getPosts(reset: Boolean = false)

    fun loadMorePosts()
}