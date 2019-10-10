package pl.adoptunek.adoptunek.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.login.LoginActivity
import pl.adoptunek.adoptunek.register.RegisterActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeStatusBarColor()
        Glide.with(this).load(getDrawable(R.drawable.corgi)).into(corgiView)
        registerBtn.setOnClickListener{startRegisterActivity()}
        loginBtn.setOnClickListener{startLoginActivity()}
    }

    private fun changeStatusBarColor(){
        val window = getWindow()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black))
    }

    private fun startRegisterActivity(){
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun startLoginActivity(){
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
