package pl.adoptunek.adoptunek.fragments.home

import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostViewHolder

interface HomeContract {

    interface HomePresenter{

        fun onBindViewHolder(holder: PostViewHolder, position: Int)

        fun getItemCount(): Int

        fun petPostClicked(position: Int)

    }

}