package pl.adoptunek.adoptunek.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.emailText
import kotlinx.android.synthetic.main.activity_login.passText
import kotlinx.android.synthetic.main.activity_register.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.login.google.GoogleContract
import pl.adoptunek.adoptunek.login.google.GoogleLoginImpl
import pl.adoptunek.adoptunek.start.StartActivity

class RegisterActivity : AppCompatActivity(), TextWatcher, RegisterContract.RegisterView,
    GoogleContract.GoogleInterractor {

    private val NEXT_BTN = 0
    private val GOOGLE_BTN = 1
    private lateinit var registerWithGoogleText: String
    private lateinit var presenter: RegisterContract.RegisterPresenter
    private lateinit var googleLogin: GoogleContract.GoogleLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerWithGoogleText = registerWithGoogleBtn.text.toString()
        presenter = RegisterPresenterImpl(this)
        googleLogin = GoogleLoginImpl(this, this)
        emailText.addTextChangedListener(this)
        passText.addTextChangedListener(this)
        registerNextBtn.setOnClickListener{presenter.nextBtnClicked(emailText.text.toString(),
            passText.text.toString(), repPassText.text.toString())}
        registerWithGoogleBtn.setOnClickListener{
            showLoadingGoogleBtn(true)
            googleLogin.loginWithGoogle()
        }
        repPassText.addTextChangedListener(this)
        registerNextBtn.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleLogin.onActivityResult(requestCode, resultCode, data)
    }

    override fun afterTextChanged(p0: Editable?) {
        registerNextBtn.isEnabled = !(passText.text.toString().isEmpty()||
                emailText.text.toString().isEmpty()||repPassText.text.toString().isEmpty())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun showLoadingNextBtn(loading: Boolean) {
        if(loading) showLoadingNextBtn()
        else showUnloadingNextBtn()
        setOtherComponentsActive(NEXT_BTN, !loading)
    }

    private fun showLoadingNextBtn(){
        progressRegisterNext.visibility = View.VISIBLE
        registerNextBtn.visibility = View.INVISIBLE
    }

    private fun showUnloadingNextBtn(){
        registerNextBtn.visibility = View.VISIBLE
        progressRegisterNext.visibility = View.GONE
    }

    private fun showLoadingGoogleBtn(loading: Boolean) {
        if(loading) showLoadingGoogleBtn()
        else showUnloadingGoogleBtn()
        setOtherComponentsActive(GOOGLE_BTN, !loading)
    }

    private fun showLoadingGoogleBtn(){
        registerWithGoogleBtn.text = ""
        progressRegisterGoogle.visibility = View.VISIBLE
    }

    private fun showUnloadingGoogleBtn(){
        registerWithGoogleBtn.text = registerWithGoogleText
        progressRegisterGoogle.visibility = View.GONE
    }

    private fun setOtherComponentsActive(whichBtn: Int, enabled: Boolean){
        val alpha = getAlphaOfEnabled(enabled)
        if(whichBtn == NEXT_BTN) registerWithGoogleBtn.alpha = alpha
        else registerNextBtn.alpha = alpha
        emailText.alpha = alpha
        passText.alpha = alpha
        repPassText.alpha = alpha
        setOtherComponentsEnabled(enabled)
    }

    private fun getAlphaOfEnabled(enabled: Boolean): Float{
        if(enabled) return 1f
        else return 0.6f
    }

    private fun setOtherComponentsEnabled(enabled: Boolean){
        registerWithGoogleBtn.isEnabled = enabled
        registerNextBtn.isEnabled = enabled
        emailText.isEnabled = enabled
        passText.isEnabled = enabled
        repPassText.isEnabled = enabled
    }

    override fun loginWithGoogleResult(successfull: Boolean, error: String) {
        showLoadingGoogleBtn(false)
        if(successfull){
            setResult(StartActivity.REGISTER_WITH_GOOGLE_SUCCESS)
            finish()
        }else Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun registerOperationResult(successfull: Boolean, error: String) {
        if(successfull){
            setResult(StartActivity.CHECK_EMAIL_RESULT)
            finish()
        } else Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}
