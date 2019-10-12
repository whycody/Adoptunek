package pl.adoptunek.adoptunek.register

import com.google.firebase.auth.*
import java.lang.Exception

class RegisterPresenterImpl(val view: RegisterContract.RegisterView): RegisterContract.RegisterPresenter {

    val auth = FirebaseAuth.getInstance()

    override fun nextBtnClicked(email: String, pass: String, repPass: String) {
        if(!pass.equals(repPass)) view.registerOperationResult(false, "Hasła nie są takie same.")
        else registerNewAccount(email, pass)
    }

    private fun registerNewAccount(email: String, pass: String){
        view.showLoadingNextBtn(true)
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{ task ->
            if(task.isSuccessful) checkEmail()
            else{
                view.showLoadingNextBtn(false)
                view.registerOperationResult(false, handleError(task.exception!!))
            }
        }
    }

    private fun checkEmail(){
        auth.currentUser!!.sendEmailVerification().addOnCompleteListener{ task ->
            view.showLoadingNextBtn(false)
            if(task.isSuccessful) view.registerOperationResult(true)
            else view.registerOperationResult(false, handleError(task.exception!!))
        }
    }

    private fun handleError(exception: Exception): String{
        when(exception){
            is FirebaseAuthWeakPasswordException -> return "Hasło jest zbyt słabe"
            is FirebaseAuthInvalidCredentialsException -> return "Podany adres e-mail jest nieprawidłowy"
            is FirebaseAuthUserCollisionException -> return "Podany adres e-mail nie jest dostępny"
            else -> return "Wystąpił nieznany błąd. Spróbuj ponownie później"
        }
    }
}