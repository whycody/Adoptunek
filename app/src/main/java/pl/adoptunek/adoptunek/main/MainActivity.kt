package pl.adoptunek.adoptunek.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.fragments.home.HomeFragment
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
        val viewPager = ViewPager(this)
        viewPager.offscreenPageLimit = 4
        mainNav.clearAnimation()
        mainNav.setOnNavigationItemSelectedListener(this)
        changeFragment(homeFragment, "home")
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.home -> {
                changeFragment(homeFragment, "home")
                return true
            }R.id.shelter -> {
                changeFragment(shelterFragment, "shelter")
                mainAppBarLayout.setExpanded(true, false)
                return true
            }R.id.library -> {
                changeFragment(libraryFragment, "library")
                mainAppBarLayout.setExpanded(true, false)
                return true
            }
            else -> return false
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun changeFragment(fragment: Fragment, tagFragmentName: String) {
        val mFragmentManager = supportFragmentManager
        val fragmentTransaction = mFragmentManager.beginTransaction()
        val currentFragment = mFragmentManager.primaryNavigationFragment
        if (currentFragment != null) fragmentTransaction.hide(currentFragment)
        var fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName)
        if (fragmentTemp == null) {
            fragmentTemp = fragment
            fragmentTransaction.add(R.id.mainFrameLayout, fragmentTemp, tagFragmentName)
        } else fragmentTransaction.show(fragmentTemp)

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
    }
}
