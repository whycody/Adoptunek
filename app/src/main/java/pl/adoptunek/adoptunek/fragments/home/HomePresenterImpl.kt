package pl.adoptunek.adoptunek.fragments.home

import android.content.Context
import android.content.Intent
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.data.post.PostDaoImpl
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder
import pl.adoptunek.adoptunek.pet.view.PetViewActivity

class HomePresenterImpl(private var postsList: List<Post>, val context: Context): HomeContract.HomePresenter{

    private val postDao = PostDaoImpl()

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList[position]
        holder.setShelterName(post.shelterName!!)
        holder.setTimeAgo(post.timeAgo!!)
        postDao.getPetUri(holder, post)
        postDao.getShelterUri(holder, post)
        holder.setOnPetImageClickListener(post.idOfAnimal!!, position)
        holder.setDataOfAnimals(post.dataOfAnimal!!)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun petPostClicked(position: Int) {
        val intent = Intent(context, PetViewActivity::class.java)
        intent.putExtra(HomeFragment.PET, postsList[position])
        context.startActivity(intent)
    }

    override fun refreshListOfPosts(list: List<Post>) {
        postsList = list
    }
}