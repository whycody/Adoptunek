package pl.adoptunek.adoptunek.register

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class RegisterPresenterImpl(val view: RegisterContract.RegisterView): RegisterContract.RegisterPresenter {

    val auth = FirebaseAuth.getInstance()

    override fun nextBtnClicked(email: String, pass: String, repPass: String) {
        if(!pass.equals(repPass)) view.registerOperationCompleted(false, "Hasła nie są takie same.")
        else registerNewAccount(email, pass)
    }

    private fun registerNewAccount(email: String, pass: String){
        view.showLoadingNextBtn(true)
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{ task ->
            view.showLoadingNextBtn(false)
            if(task.isSuccessful) view.registerOperationCompleted(true)
            else view.registerOperationCompleted(false, handleError(task))
        }
    }

    private fun handleError(task: Task<AuthResult>): String{
        when(task.exception){
            is FirebaseAuthWeakPasswordException -> return "Hasło jest zbyt słabe"
            is FirebaseAuthInvalidCredentialsException -> return "Podany adres e-mail jest nieprawidłowy"
            is FirebaseAuthUserCollisionException -> return "Podany adres e-mail nie jest dostępny"
            else -> return "Wystąpił nieznany błąd. Spróbuj ponownie później"
        }
    }

    override fun loginWithGoogleClicked() {
        view.showLoadingGoogleBtn(true)
    }
}