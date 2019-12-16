package pl.adoptunek.adoptunek.data.post

interface PostDao {

    fun getPosts(count: Int)

    fun loadMorePosts()
}