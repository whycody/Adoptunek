package pl.adoptunek.adoptunek.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.emailText
import kotlinx.android.synthetic.main.activity_register.passText
import pl.adoptunek.adoptunek.R

class LoginActivity : AppCompatActivity(), TextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailText.addTextChangedListener(this)
        passText.addTextChangedListener(this)
        nextBtn.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
    }

    override fun afterTextChanged(p0: Editable?) {
        nextBtn.isEnabled = !(passText.text.toString().isEmpty()|| emailText.text.toString().isEmpty())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}
