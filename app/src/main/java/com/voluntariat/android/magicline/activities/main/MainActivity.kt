package com.voluntariat.android.magicline.activities.main

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import com.voluntariat.android.magicline.R
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import com.voluntariat.android.magicline.activities.main.fragments.*


class MainActivity : AppCompatActivity() {

    //Bottom Toolbar
    private lateinit var bottomBarView: com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
    private lateinit var bottomBarBtn: FloatingActionButton

    //Top Toolbar
//    private lateinit var topBarView: com.android.


    //The app starts at the magic line fragment
    private var currentFragment: Fragment = DetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidgets()

        initBottomBar()

        initNavigation()
    }

    private fun initWidgets() {
        //BottomBar
        bottomBarView = findViewById(R.id.bottom_navigation)
        bottomBarBtn = findViewById(R.id.fab)

        //TopBar
//        topBarView = findViewById(R.id.top_toolbar)
//        topBarBtn = findViewById(R.id.backArrow)
    }

    override fun onBackPressed() {
        val count = fragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            fragmentManager.popBackStack()
        }
    }

    fun back() {
        val count = fragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            fragmentManager.popBackStack()
        }
    }

    private fun initBottomBar() {

        bottomBarView.enableShiftingMode(false)
        bottomBarView.enableItemShiftingMode(false)
        bottomBarView.setTextSize(9.0f)

    }

    private fun initNavigation() {

        //First time we open the app
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, MagicLineFragment())
        transaction.commit()

        //Behaviour when clicked on a item different from map
        bottomBarView.setOnNavigationItemSelectedListener { item ->
            bottomBarBtn.setColorFilter(Color.argb(255,74,74,74))

            selectFragment(item)
            true
        }

        //Behaviour when clicked on the map item
        bottomBarBtn.setOnClickListener {

            bottomBarBtn.setColorFilter(Color.argb(255,237,53,37))

            //We set clicked on the none item in order to disable the rest of the items
            //but the fragment that is shown is the map fragment
            bottomBarView.menu.getItem(2).isChecked = true

            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, MapFragment())
            transaction.commit()
        }

//        topBarBtn.setOnClickListener {
//            val fragmentManager = this.supportFragmentManager
//            fragmentManager.popBackStack()
//        }

    }

    private fun selectFragment(item: MenuItem) {

        val newFragment: Fragment

        when (item.itemId) {
            R.id.magicline_menu_id -> {
                newFragment = MagicLineFragment()
                Log.d("Main Activity", "magic line")
            }
            R.id.donations_menu_id -> {
                newFragment = DonationsFragment()
                Log.d("Main Activity", "donations")
            }
            R.id.info_menu_id -> {
                newFragment = InfoFragment()
                Log.d("Main Activity", "info")
            }
            R.id.schedule_menu_id -> newFragment = ScheduleFragment()
            R.id.none -> return
            else -> newFragment = MagicLineFragment()
        }


        val trans = this.supportFragmentManager.beginTransaction()
        trans.replace(R.id.frame_layout, newFragment)
        trans.commit()

        currentFragment = newFragment



    }

}
