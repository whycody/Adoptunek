package pl.adoptunek.adoptunek.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.adoptunek.adoptunek.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
    }
}
