package pl.adoptunek.adoptunek.fragments.home

import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder

class HomePresenterImpl(val postsList: List<Post>): HomeContract.HomePresenter {

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList.get(position)
        holder.setShelterName(post.shelterName!!)
        holder.setTimeAgo(post.timeAgo!!)
        holder.setShelterImage(post.shelterUri!!)
        holder.setPetImage(post.petUri!!)
        holder.setOnPetImageClickListener(post.idOfAnimal!!)
        holder.setDataOfAnimals(post.dataOfAnimal!!)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }
}