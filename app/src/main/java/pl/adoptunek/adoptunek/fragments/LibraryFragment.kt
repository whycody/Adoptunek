package pl.adoptunek.adoptunek.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_library.view.*

import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.start.StartActivity

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        view.signOutBtn.setOnClickListener{signOut()}
        return view
    }

    private fun signOut(){
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null) {
            val googleSignInClient = GoogleSignIn.getClient(activity!!,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            auth.signOut()
            googleSignInClient.signOut()
            startActivity(Intent(activity, StartActivity::class.java))
            activity!!.finish()
        }
    }
}
