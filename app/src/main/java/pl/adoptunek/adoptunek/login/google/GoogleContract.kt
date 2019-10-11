package pl.adoptunek.adoptunek.login.google

import android.content.Intent

interface GoogleContract {

    interface GoogleInterractor {
        fun loginWithGoogleResult(successfull: Boolean, error: String = "empty")
    }

    interface GoogleLogin {
        fun loginWithGoogle()

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}