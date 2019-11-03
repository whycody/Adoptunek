package pl.adoptunek.adoptunek.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
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
        setSupportActionBar(mainToolbar as Toolbar)
        val viewPager = ViewPager(this)
        viewPager.offscreenPageLimit = 4
        mainNav.clearAnimation()
        mainNav.setOnNavigationItemSelectedListener(this)
        changeFragment(homeFragment, "home")
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return when(p0.itemId){
            R.id.home -> {
                changeFragment(homeFragment, "home")
                true
            }R.id.shelter -> {
                changeFragment(shelterFragment, "shelter")
                //TODO ask for the permissions
                mainAppBarLayout.setExpanded(true, false)
                true
            }R.id.library -> {
                changeFragment(libraryFragment, "library")
                mainAppBarLayout.setExpanded(true, false)
                true
            }
            else -> false
        }
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
