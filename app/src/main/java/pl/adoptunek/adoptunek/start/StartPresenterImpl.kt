package pl.adoptunek.adoptunek.start

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.start.StartActivity.Companion.REGISTER_REQUEST

class StartPresenterImpl(val context: Context): StartContract.StartPresenter {

    private val auth = FirebaseAuth.getInstance()

    override fun showVerifyEmailAlertDialog(requestCode: Int) {
        val auth = FirebaseAuth.getInstance()
        val builder = AlertDialog.Builder(context)
            .setTitle(getTitle(requestCode))
            .setMessage("Wysłaliśmy wiadomość e-mail na adres" +
                    " ${auth.currentUser?.email}. " +
                    "Zawiera ona odnośnik, na który trzeba kliknąć, w celu aktywowania konta.")
            .setNegativeButton("OK", null)
            .setPositiveButton("Wyślij ponownie"){dialog, which ->
                sendEmailAgain()
            }
        val alertDialog = builder.create()
        alertDialog.setOnShowListener{ arg ->
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        }
        alertDialog.show()
    }

    private fun sendEmailAgain(){
        auth.currentUser!!.sendEmailVerification().addOnCompleteListener{ task ->
            if(task.isSuccessful) Toast.makeText(context, "Wiadomość e-mail została wysłana ponownie" +
                    " na adres ${auth.currentUser?.email}.", Toast.LENGTH_SHORT).show()
            else Toast.makeText(context, "Wystąpił nieznany błąd. Spróbuj ponownie później.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTitle(requestCode: Int): String{
        if(requestCode==REGISTER_REQUEST) return "Zarejestrowano pomyślnie"
        else return "Sprawdź swoją skrzynkę"
    }

}