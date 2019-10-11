package pl.adoptunek.adoptunek.login.google

interface GoogleContract {

    interface GoogleInterractor {
        fun loginWithGoogleSuccessfull(): Boolean
    }

    interface GoogleView {
        fun loginWithGoogle()
    }
}