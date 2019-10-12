package pl.adoptunek.adoptunek.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import pl.adoptunek.adoptunek.main.MainActivity
import pl.adoptunek.adoptunek.start.StartActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        checkUser()
        finish()
    }

    private fun checkUser(){
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null&&auth.currentUser!!.isEmailVerified)
            startActivity(Intent(this, MainActivity::class.java))
        else startActivity(Intent(this, StartActivity::class.java))
    }
}
