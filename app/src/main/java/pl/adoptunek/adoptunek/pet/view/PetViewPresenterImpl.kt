package pl.adoptunek.adoptunek.pet.view

import android.content.Context
import android.net.Uri
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.data.pet.PetContract
import pl.adoptunek.adoptunek.data.pet.PetDaoImpl
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PetViewPresenterImpl(val context: Context, val petView: PetViewContract.PetView):
    PetViewContract.PetViewPresenter, PetContract.PetInterractor {

    private lateinit var post: Post
    private val petDao = PetDaoImpl(this)
    private val timeHelper = TimeHelperImpl()

    override fun onCreate() {
        post = petView.getPost()
        petView.loadPetImage(Uri.parse(post.petUri))
        petView.setTitle(post.petName!!)
        petDao.getDocumentWithPet(post.idOfAnimal!!, false)
    }

    override fun listWithPetsIsReady(successfully: Boolean, petList: List<Pet>?) {

    }

    override fun petDocumentIsReady(successfully: Boolean, pet: Pet?) {
        if(successfully){
            putViewsToFlexboxLayout(getDataOfPet(pet!!))
            if(pet.describe!=null) petView.setDescribeText(pet.describe!!)
        }
    }

    private fun getDataOfPet(pet: Pet): List<Pair<String, String>>{
        val list = mutableListOf<Pair<String, String>>()
        list.add(Pair("Imię", pet.name!!))
        list.add(Pair("Płeć", pet.sex!!))
        list.add(Pair("Wiek", "2-3 lata")) //DO ZROBIENIA
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

}