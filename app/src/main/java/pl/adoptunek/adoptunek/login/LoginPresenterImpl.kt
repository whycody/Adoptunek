package pl.adoptunek.adoptunek.login

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*

class LoginPresenterImpl(val view: LoginContract.LoginView): LoginContract.LoginPresenter {
    private val auth = FirebaseAuth.getInstance()

    override fun nextBtnClicked(email: String, pass: String) {
        view.showLoadingNextBtn(true)
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{task ->
            view.showLoadingNextBtn(false)
            if(task.isSuccessful) view.loginOperationResult(true)
            else view.loginOperationResult(false, handleError(task))
        }
    }

    private fun handleError(task: Task<AuthResult>): String{
        when(task.exception){
            is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException ->
                return ("Wprowadzono błędny adres e-mail lub hasło.")
            is FirebaseNetworkException -> return ("Sprawdź swoje łącze internetowe i spróbuj ponownie później.")
            else -> return ("Wystąpił nieznany błąd. Spróbuj ponownie później.")
        }
    }
}