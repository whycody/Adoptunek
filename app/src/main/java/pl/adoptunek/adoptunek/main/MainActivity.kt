package pl.adoptunek.adoptunek.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.common.io.Resources
import kotlinx.android.synthetic.main.activity_main.*
import pl.adoptunek.adoptunek.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeStatusBarColor()
        Glide.with(this).load(getDrawable(R.drawable.corgi)).into(corgiView)
    }

    private fun changeStatusBarColor(){
        val window = getWindow()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black))
    }
}
