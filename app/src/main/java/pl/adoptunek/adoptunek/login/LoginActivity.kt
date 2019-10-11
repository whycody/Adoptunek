package pl.adoptunek.adoptunek.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.emailText
import kotlinx.android.synthetic.main.activity_register.passText
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.login.google.GoogleContract
import pl.adoptunek.adoptunek.login.google.GoogleLoginImpl

class LoginActivity : AppCompatActivity(), TextWatcher, LoginContract.LoginView, GoogleContract.GoogleInterractor {

    private lateinit var loginWithGoogleBtnText: String
    private lateinit var presenter: LoginContract.LoginPresenter
    private lateinit var googleLogin: GoogleContract.GoogleLogin
    private val NEXT_BTN = 0
    private val GOOGLE_BTN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginWithGoogleBtnText = loginWithGoogleBtn.text.toString()
        presenter = LoginPresenterImpl(this)
        googleLogin = GoogleLoginImpl(this, this)
        emailText.addTextChangedListener(this)
        passText.addTextChangedListener(this)
        nextBtn.setOnClickListener{presenter.nextBtnClicked(emailText.text.toString(), passText.text.toString())}
        loginWithGoogleBtn.setOnClickListener{
            showLoadingGoogleBtn(true)
            googleLogin.loginWithGoogle()
        }
        nextBtn.isEnabled = false
        signOut()
    }

    private fun signOut(){
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null) {
            val googleSignInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            auth.signOut()
            googleSignInClient.signOut()
        }
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
        nextBtn.isEnabled = !(passText.text.toString().isEmpty()|| emailText.text.toString().isEmpty())
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
        progressLoginNext.visibility = View.VISIBLE
        nextBtn.visibility = View.INVISIBLE
    }

    private fun showUnloadingNextBtn(){
        nextBtn.visibility = View.VISIBLE
        progressLoginNext.visibility = View.GONE
    }

    private fun showLoadingGoogleBtn(loading: Boolean) {
        if(loading) showLoadingGoogleBtn()
        else showUnloadingGoogleBtn()
        setOtherComponentsActive(GOOGLE_BTN, !loading)
    }

    private fun showLoadingGoogleBtn(){
        loginWithGoogleBtn.text = ""
        progressLoginGoogle.visibility = View.VISIBLE
    }

    private fun showUnloadingGoogleBtn(){
        loginWithGoogleBtn.text = loginWithGoogleBtnText
        progressLoginGoogle.visibility = View.GONE
    }

    private fun setOtherComponentsActive(whichBtn: Int, enabled: Boolean){
        var alpha = getAlphaOfEnabled(enabled)
        if(whichBtn == NEXT_BTN) loginWithGoogleBtn.alpha = alpha
        else nextBtn.alpha = alpha
        emailText.alpha = alpha
        passText.alpha = alpha
        setOtherComponentsEnabled(enabled)
    }

    private fun getAlphaOfEnabled(enabled: Boolean): Float{
        if(enabled) return 1f
        else return 0.6f
    }

    private fun setOtherComponentsEnabled(enabled: Boolean){
        loginWithGoogleBtn.isEnabled = enabled
        nextBtn.isEnabled = enabled
        emailText.isEnabled = enabled
        passText.isEnabled = enabled
    }

    override fun loginWithGoogleResult(successfull: Boolean, error: String) {
        showLoadingGoogleBtn(false)
        loginOperationCompleted(successfull, error)
    }

    override fun loginOperationCompleted(successfull: Boolean, error: String) {
        if(successfull) Toast.makeText(this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}
