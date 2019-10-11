package pl.adoptunek.adoptunek.login.google

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import pl.adoptunek.adoptunek.R
import java.lang.Exception

class GoogleLoginImpl(val activity: Activity, val interractor: GoogleContract.GoogleInterractor):
    GoogleContract.GoogleLogin {

    private val GOOGLE_ID = 1
    private val auth = FirebaseAuth.getInstance()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    override fun loginWithGoogle() {
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, GOOGLE_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GOOGLE_ID){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                interractor.loginWithGoogleResult(false, handleError(task.exception!!))
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(googleCompleteListener)
    }

    private val googleCompleteListener = object: OnCompleteListener<AuthResult> {
        override fun onComplete(p0: Task<AuthResult>) {
            if(p0.isSuccessful) interractor.loginWithGoogleResult(true)
            else interractor.loginWithGoogleResult(false, handleError(p0.exception!!))
        }
    }

    private fun handleError(exception: Exception): String{
        when(exception){
            is FirebaseNetworkException -> return ("Sprawdź swoje łącze internetowe i spróbuj ponownie później.")
            else -> return ("Wystąpił nieznany błąd. Spróbuj ponownie później.")
        }
    }
}