package pl.adoptunek.adoptunek.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_login.emailText
import kotlinx.android.synthetic.main.activity_login.passText
import kotlinx.android.synthetic.main.activity_register.*
import pl.adoptunek.adoptunek.R

class RegisterActivity : AppCompatActivity(), TextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        emailText.addTextChangedListener(this)
        passText.addTextChangedListener(this)
        repPassText.addTextChangedListener(this)
        registerNextBtn.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
    }

    override fun afterTextChanged(p0: Editable?) {
        registerNextBtn.isEnabled = !(passText.text.toString().isEmpty()||
                emailText.text.toString().isEmpty()||repPassText.text.toString().isEmpty())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}
