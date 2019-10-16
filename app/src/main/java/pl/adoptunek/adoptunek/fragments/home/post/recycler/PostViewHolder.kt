package pl.adoptunek.adoptunek.fragments.home.post.recycler

import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.adoptunek.adoptunek.R

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PostRowView {

    override fun setShelterName(name: String) {
        itemView.findViewById<TextView>(R.id.shelterText).text = name
    }

    override fun setTimeAgo(time: String) {
        itemView.findViewById<TextView>(R.id.timeAgoText).text = time
    }

    override fun setShelterImage(uri: Uri) {
        Glide.with(itemView.context).load(uri).into(itemView.findViewById(R.id.shelterImage))
    }

    override fun setPetImage(uri: Uri) {
        Glide.with(itemView.context).load(uri).into(itemView.findViewById(R.id.petImage))
    }

}