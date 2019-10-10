package pl.adoptunek.adoptunek.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.adoptunek.adoptunek.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
    }
}
