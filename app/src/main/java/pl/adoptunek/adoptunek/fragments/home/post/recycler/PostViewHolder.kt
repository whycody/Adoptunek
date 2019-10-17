package pl.adoptunek.adoptunek.fragments.home.post.recycler

import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
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

    override fun setDataOfAnimals(data: List<Pair<String, String>>) {
        for(item in data){
            val textView = getDefaultTextView()
            textView.setText("${item.first}: ${item.second}")
            itemView.findViewById<FlexboxLayout>(R.id.flexBoxDataLayout).addView(textView)
        }
    }

    private fun getDefaultTextView(): TextView{
        val textView = TextView(itemView.context)
        val params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0,0,20,20)
        textView.layoutParams = params
        textView.setPadding(18,10,18,10)
        textView.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
        textView.background = itemView.context.getDrawable(R.drawable.animal_item_data_drw)
        textView.textSize = 14f
        return textView
    }

}