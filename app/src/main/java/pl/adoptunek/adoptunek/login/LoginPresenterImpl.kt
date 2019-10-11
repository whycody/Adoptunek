package pl.adoptunek.adoptunek.login

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginPresenterImpl(val view: LoginContract.LoginView): LoginContract.LoginPresenter {

    val auth = FirebaseAuth.getInstance()

    override fun nextBtnClicked(email: String, pass: String) {
        view.showLoadingNextBtn(true)
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{task ->
            view.showLoadingNextBtn(false)
            if(task.isSuccessful) view.loginOperationCompleted(true)
            else view.loginOperationCompleted(false, handleError(task))
        }
    }

    private fun handleError(task: Task<AuthResult>): String{
        when(task.exception){
            is FirebaseAuthInvalidUserException -> return ("Wprowadzono błędny adres e-mail lub hasło.")
            is FirebaseNetworkException -> return ("Sprawdź swoje łącze internetowe i spróbuj ponownie później.")
            else -> return ("Wystąpił nieznany błąd. Spróbuj ponownie później.")
        }
    }

    override fun loginWithGoogleBtnClicked() {
        view.showLoadingGoogleBtn(true)
    }
}