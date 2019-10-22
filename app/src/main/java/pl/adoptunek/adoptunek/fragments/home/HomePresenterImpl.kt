package pl.adoptunek.adoptunek.fragments.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder
import pl.adoptunek.adoptunek.pet.view.PetViewActivity

class HomePresenterImpl(val postsList: List<Post>, val context: Context): HomeContract.HomePresenter {

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList.get(position)
        holder.setShelterName(post.shelterName!!)
        holder.setTimeAgo(post.timeAgo!!)
        holder.setShelterImage(Uri.parse(post.shelterUri!!))
        holder.setPetImage(Uri.parse(post.petUri!!))
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
}