package pl.adoptunek.adoptunek.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_start.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.login.LoginActivity
import pl.adoptunek.adoptunek.register.RegisterActivity

class StartActivity : AppCompatActivity() {

    private val presenter = StartPresenterImpl(this)

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
            presenter.showVerifyEmailAlertDialog(requestCode)
        else if(resultCode == REGISTER_WITH_GOOGLE_SUCCESS || resultCode == LOGIN_SUCCESS)
            finish()
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
        val REGISTER_REQUEST = 0
        val LOGIN_REQUEST = 1
        val CHECK_EMAIL_RESULT = 3
        val REGISTER_WITH_GOOGLE_SUCCESS = 4
        val LOGIN_SUCCESS = 5
    }
}
