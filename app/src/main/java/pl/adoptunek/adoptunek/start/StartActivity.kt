package pl.adoptunek.adoptunek.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.login.LoginActivity
import pl.adoptunek.adoptunek.register.RegisterActivity

class StartActivity : AppCompatActivity() {

    private val REGISTER_REQUEST = 0
    private val LOGIN_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        changeStatusBarColor()
        Glide.with(this).load(getDrawable(R.drawable.corgi)).into(corgiView)
        registerBtn.setOnClickListener{startRegisterActivity()}
        loginBtn.setOnClickListener{startLoginActivity()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== CHECK_EMAIL_RESULT)
            showDialogEmailToVerify(requestCode)
        else if(resultCode == REGISTER_WITH_GOOGLE_SUCCESS || resultCode == LOGIN_SUCCESS)
            finish()
    }

    private fun showDialogEmailToVerify(requestCode: Int){
        val auth = FirebaseAuth.getInstance()
        val builder = AlertDialog.Builder(this)
            .setTitle(getTitle(requestCode))
            .setMessage("Wysłaliśmy wiadomość e-mail na adres" +
                    " \"${auth.currentUser?.email}\". " +
                    "Zawiera ona odnośnik, na który trzeba kliknąć, w celu aktywowania konta.")
            .setNegativeButton("OK", null)
            .setPositiveButton("Wyślij ponownie"){dialog, which ->
                sendEmailAgain()
            }.show()
    }

    private fun sendEmailAgain(){

    }

    private fun getTitle(requestCode: Int): String{
        if(requestCode==REGISTER_REQUEST) return "Zarejestrowano pomyślnie"
        else return "Sprawdź swoją skrzynkę"
    }

    private fun changeStatusBarColor(){
        val window = getWindow()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black))
    }

    private fun startRegisterActivity(){
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        startActivityForResult(Intent(this, RegisterActivity::class.java), REGISTER_REQUEST)
    }

    private fun startLoginActivity(){
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN_REQUEST)
    }

    companion object{
        val CHECK_EMAIL_RESULT = 3
        val REGISTER_WITH_GOOGLE_SUCCESS = 4
        val LOGIN_SUCCESS = 5
    }
}
