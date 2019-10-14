package pl.adoptunek.adoptunek.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.fragments.HomeFragment
import pl.adoptunek.adoptunek.fragments.LibraryFragment
import pl.adoptunek.adoptunek.fragments.shelter.ShelterFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val homeFragment = HomeFragment()
    private val shelterFragment = ShelterFragment()
    private val libraryFragment = LibraryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        val navigationHelper = BottomNavigationViewHelper()
        navigationHelper.disableShiftMode(mainNav)
        mainNav.clearAnimation()
        mainNav.setOnNavigationItemSelectedListener(this)
        setFragment(getFragmentOfID(mainNav.selectedItemId))
    }

    private fun getFragmentOfID(id: Int): Fragment{
        when(id){
            R.id.home -> return homeFragment
            R.id.shelter -> return shelterFragment
            else -> return libraryFragment
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.home -> {
                setFragment(homeFragment)
                return true
            }R.id.shelter -> {
                setFragment(shelterFragment)
                return true
            }R.id.library -> {
                setFragment(libraryFragment)
                return true
            }
            else -> return false
        }
    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}
