package pl.adoptunek.adoptunek.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.start.StartActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signOutBtn.setOnClickListener{signOut()}
    }

    private fun signOut(){
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null) {
            val googleSignInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            auth.signOut()
            googleSignInClient.signOut()
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}
