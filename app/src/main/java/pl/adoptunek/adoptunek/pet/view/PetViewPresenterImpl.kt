package pl.adoptunek.adoptunek.pet.view

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayout
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.data.converter.PetConverterImpl
import pl.adoptunek.adoptunek.data.pet.PetContract
import pl.adoptunek.adoptunek.data.pet.PetDaoImpl

class PetViewPresenterImpl(val context: Context, val petView: PetViewContract.PetView):
    PetViewContract.PetViewPresenter,
    PetContract.PetGalleryInterractor {

    private lateinit var post: Post
    private val petDao = PetDaoImpl(this)
    private val petConverter = PetConverterImpl()
    private lateinit var res: Resources
    private var pxFromDp = 0

    override fun onCreate() {
        post = petView.getPost()
        res = context.resources
        pxFromDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17f, res.displayMetrics).toInt()
        petView.setTitle(post.petName!!)
        if(post.petUri!=null) petView.loadPetImage(Uri.parse(post.petUri))
        else petView.loadPetImage()
        petConverter.addFullDataToPost(post)
        putViewsToFlexboxLayout(post.dataOfAnimal!!)
        petDao.getPhotosOfPet(post.idOfAnimal!!)
        petView.showShelterFooterInLayout()
        if(post.pet?.describe!=null)
            petView.addViewToLinearLayout(getDescribeTextView(post.pet?.describe!!))
    }

    override fun photoIsReady(uri: Uri, index: Int) {
        val requestOptions = RequestOptions().transform(RoundedCorners(50))
        val imageView = getDefaultImageView(index)
        petView.addViewToLinearLayout(imageView)
        Glide.with(context)
            .load(uri)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    private fun getDefaultImageView(index: Int): ImageView{
        val imageView = ImageView(context)
        val marginParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT) as ViewGroup.MarginLayoutParams
        imageView.setPadding(pxFromDp,30,pxFromDp,0)
        if(index==0) marginParams.topMargin = 40
        imageView.layoutParams = marginParams
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_START
        return imageView
    }

    private fun putViewsToFlexboxLayout(list: List<Pair<String, String>>){
        for(item in list){
            val textView = getDefaultFlexboxTextView()
            textView.text = "${item.first}: ${item.second}"
            petView.addViewToFlexboyLayout(textView)
        }
    }

    private fun getDefaultFlexboxTextView(): TextView {
        val textView = TextView(context)
        val params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0,0,20,20)
        textView.layoutParams = params
        textView.setPadding(18,15,18,15)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        textView.background = context.getDrawable(R.drawable.animal_item_data_drw)
        textView.textSize = 13f
        return textView
    }

    private fun getDescribeTextView(describe: String): TextView {
        val textView = TextView(context)
        val marginParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        marginParams.setMargins(pxFromDp,40,pxFromDp,0)
        textView.layoutParams = marginParams
        textView.text = describe
        textView.textSize = 16f
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        textView.letterSpacing = 0.01f
        textView.alpha = 0.6f
        textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f,
            context.resources.displayMetrics), 1.0f)
        return textView
    }

}