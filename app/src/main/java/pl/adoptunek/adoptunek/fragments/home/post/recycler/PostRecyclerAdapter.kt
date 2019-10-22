package pl.adoptunek.adoptunek.fragments.home.post.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.fragments.home.HomeContract

class PostRecyclerAdapter(val presenter: HomeContract.HomePresenter, val context: Context):
    RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, presenter)
    }

    override fun getItemCount(): Int {
        return presenter.getItemCount()
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        presenter.onBindViewHolder(holder, position)
    }
}