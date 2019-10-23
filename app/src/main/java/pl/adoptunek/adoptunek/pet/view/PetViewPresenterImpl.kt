package pl.adoptunek.adoptunek.pet.view

import android.content.Context
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
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.data.pet.PetContract
import pl.adoptunek.adoptunek.data.pet.PetDaoImpl
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PetViewPresenterImpl(val context: Context, val petView: PetViewContract.PetView):
    PetViewContract.PetViewPresenter, PetContract.PetObjectsInterractor, PetContract.PetGalleryInterractor {

    private lateinit var post: Post
    private val petDao = PetDaoImpl(this, this)
    private val timeHelper = TimeHelperImpl()

    override fun onCreate() {
        post = petView.getPost()
        petView.loadPetImage(Uri.parse(post.petUri))
        petView.setTitle(post.petName!!)
        petDao.getDocumentWithPet(post.idOfAnimal!!, false)
        petDao.getPhotosOfPet(post.idOfAnimal!!)
        petView.showShelterFooterInLayout()
    }

    override fun listWithPetsIsReady(successfully: Boolean, petList: List<Pet>?) {

    }

    override fun petDocumentIsReady(successfully: Boolean, pet: Pet?) {
        if(successfully){
            putViewsToFlexboxLayout(getDataOfPet(pet!!))
            if(pet.describe!=null){
                petView.addViewToLinearLayout(getDescribeHeaderTextView())
                petView.addViewToLinearLayout(getDescribeTextView(pet.describe!!))
            }
        }
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

    override fun listWithPhotosIsReady(successfully: Boolean, list: List<Uri>) {

    }

    private fun getDefaultImageView(index: Int): ImageView{
        val imageView = ImageView(context)
        val marginParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT) as ViewGroup.MarginLayoutParams
        imageView.setPadding(0,30,0,0)
        if(index==0) marginParams.topMargin = 80
        imageView.layoutParams = marginParams
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_START
        return imageView
    }

    private fun getDataOfPet(pet: Pet): List<Pair<String, String>>{
        val list = mutableListOf<Pair<String, String>>()
        list.add(Pair("Imię", pet.name!!))
        list.add(Pair("Płeć", pet.sex!!))
        if(pet.birth_date!=null) list.add(Pair("Wiek", timeHelper.howLongIsWaiting(pet.birth_date!!)))
        if(pet.in_shelter!=null) list.add(Pair("Czeka", timeHelper.howLongIsWaiting(pet.in_shelter!!)))
        if(pet.siblings!=null) list.add(Pair("Rodzeństwo", pet.siblings!!))
        if(pet.full_health!=null) list.add(Pair("W pełni zdrowia", pet.full_health!!))
        if(pet.character!=null) list.add(Pair("Charakter", pet.character!!))
        if(pet.attitude_to_people!=null) list.add(Pair("Stosunek do ludzi", pet.attitude_to_people!!))
        if(pet.breed!=null) list.add(Pair("Rasa", pet.breed))
        if(pet.coat!=null) list.add(Pair("Umaszczenie", pet.coat!!))
        return list
    }

    private fun putViewsToFlexboxLayout(list: List<Pair<String, String>>){
        for(item in list){
            val textView = getDefaultTextView()
            textView.text = "${item.first}: ${item.second}"
            petView.addViewToFlexboyLayout(textView)
        }
    }

    private fun getDefaultTextView(): TextView {
        val textView = TextView(context)
        val params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0,0,20,20)
        textView.layoutParams = params
        textView.setPadding(18,15,18,15)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        textView.background = context.getDrawable(R.drawable.animal_item_data_drw)
        textView.textSize = 14f
        return textView
    }

    private fun getDescribeHeaderTextView(): TextView {
        val textView = TextView(context)
        val marginParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT) as ViewGroup.MarginLayoutParams
        marginParams.topMargin = 40
        textView.layoutParams = marginParams
        textView.text = "OPIS"
        textView.textSize = 23f
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        textView.alpha = 0.8f
        textView.requestLayout()
        return textView
    }

    private fun getDescribeTextView(describe: String): TextView {
        val textView = TextView(context)
        val marginParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT) as ViewGroup.MarginLayoutParams
        marginParams.topMargin = 15
        textView.layoutParams = marginParams
        textView.text = describe
        textView.textSize = 16f
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        textView.alpha = 0.6f
        textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f,
            context.resources.displayMetrics), 1.0f);
        return textView
    }

}