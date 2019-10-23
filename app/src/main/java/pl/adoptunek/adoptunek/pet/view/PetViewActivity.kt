package pl.adoptunek.adoptunek.pet.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import pl.adoptunek.adoptunek.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_pet_view.*
import kotlinx.android.synthetic.main.activity_pet_view.petImage
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.fragments.home.HomeFragment
import kotlin.math.abs

class PetViewActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener, PetViewContract.PetView {

    private val presenter = PetViewPresenterImpl(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_view)
        presenter.onCreate()
        changeStatusBarColor()
        setSupportActionBar(petToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.setStatusBarScrimColor(ContextCompat.getColor(this, android.R.color.transparent))
        petAppBar.addOnOffsetChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pet_view_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeStatusBarColor(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        val maxScroll = p0!!.totalScrollRange
        val percentage = abs(p1).toFloat() / maxScroll.toFloat()
        petImage.alpha = 2.5f - (percentage*2.5f)
        scrimView.alpha = 2.5f - (percentage*2.5f)
    }

    override fun getPost(): Post {
        return intent.getSerializableExtra(HomeFragment.PET) as Post
    }

    override fun setTitle(title: String) {
        collapsingToolbar.title = title
    }

    override fun loadPetImage(uri: Uri) {
        Glide.with(this).load(uri).into(petImage)
    }

    override fun addViewToFlexboyLayout(view: View) {
        petFlexboxLayout.addView(view)
    }

    override fun addViewToLinearLayout(view: View) {
        petLinearLayout.addView(view)
    }

}
