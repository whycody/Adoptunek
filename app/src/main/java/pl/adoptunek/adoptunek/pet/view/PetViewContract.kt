package pl.adoptunek.adoptunek.pet.view

import android.net.Uri
import android.view.View
import pl.adoptunek.adoptunek.Post

interface PetViewContract {

    interface PetViewPresenter {

        fun onCreate()

    }

    interface PetView {

        fun getPost(): Post

        fun setTitle(title: String)

        fun loadPetImage(uri: Uri)

        fun addViewToFlexboyLayout(view: View)

        fun setDescribeText(text: String)

    }
}