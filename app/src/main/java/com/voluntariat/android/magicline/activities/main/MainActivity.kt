package com.voluntariat.android.magicline.activities.main

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.MenuItem
import com.voluntariat.android.magicline.R
import com.voluntariat.android.magicline.R.string.lorem_ipsum
import com.voluntariat.android.magicline.activities.main.fragments.*
import com.voluntariat.android.magicline.data.MagicLineRepositoryImpl
import com.voluntariat.android.magicline.data.Result
import com.voluntariat.android.magicline.data.api.MagicLineAPI
import com.voluntariat.android.magicline.models.DetailModel
import com.voluntariat.android.magicline.models.ScheduleCardModel
import com.voluntariat.android.magicline.models.ScheduleGeneralModel
import com.voluntariat.android.magicline.models.ScheduleTextModel
import com.voluntariat.android.magicline.utils.transitionWithModalAnimation
import kotlinx.android.synthetic.main.toolbar_bottom_nav.*

class MainActivity : BaseActivity() {

    //The app starts at the magic line fragment
    private var currentFragment: Fragment = MagicLineFragment()
    private var scheduleModel: Array<ScheduleGeneralModel> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        getData()

        initBottomBar()

        if (savedInstanceState == null) {
            transitionWithModalAnimation(fragment = MagicLineFragment(), useModalAnimation = false, addToBackStack = false)
        }
        initNavigation()

        /**
         * TODO remove. Example of how to call API
         */
        val repo = MagicLineRepositoryImpl(MagicLineAPI.service)
        repo.authenticate(
                "apiml",
                "4p1ml2018"
        ) { result ->
            when (result) {
                is Result.Success -> Log.d("apiLogin", "Success, token: ${result.data}")
                is Result.Failure -> Log.d("apiLogin", "Failure: ${result.throwable.message}")
            }
        }
        repo.getPosts { result ->
            when (result) {
                is Result.Success -> Log.d("apiLogin", "Success, token: ${result.data}")
                is Result.Failure -> Log.d("apiLogin", "Failure: ${result.throwable.message}")
            }
        }
    }

    private fun getData() {
        scheduleModel = arrayOf(
                ScheduleTextModel("9:30", "Salida"),
                ScheduleCardModel("10:30", "Picnik", "Equipaments culturals obren les portes", getString(lorem_ipsum),
                        detailModel = DetailModel(
                                title = getString(R.string.essentials_title),
                                subtitle = getString(R.string.essentials_subtitle),
                                textBody = getString(R.string.essentials_body),
                                link = getString(R.string.essentials_viewOnWeb))),
                ScheduleTextModel("12:30", "Tornar a caminar"),
                ScheduleCardModel(
                        "13:30",
                        "Espectacle",
                        "Equipaments culturals obren les portes",
                        "In recent years people have realized the importance of proper diet and exercise, and recent surveys",
                        DetailModel(
                                title = getString(R.string.essentials_title),
                                subtitle = getString(R.string.essentials_subtitle),
                                textBody = getString(R.string.essentials_body),
                                link = getString(R.string.essentials_viewOnWeb))
                ),
                ScheduleTextModel("15:00", "Caminar una mica més"),
                ScheduleCardModel(
                        "16:30",
                        "Concerts",
                        "Equipaments culturals obren les portes",
                        "In recent years people",
                        detailModel = DetailModel(
                                title = getString(R.string.essentials_title),
                                subtitle = getString(R.string.essentials_subtitle),
                                textBody = getString(R.string.essentials_body),
                                link = getString(R.string.essentials_viewOnWeb))
                )
        )
    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun initBottomBar() {

        bottomBarView.enableShiftingMode(false)
        bottomBarView.enableItemShiftingMode(false)
        bottomBarView.setTextSize(9.0f)

    }

    private fun initNavigation() {

        //Behaviour when clicked on a item different from map
        bottomBarView.setOnNavigationItemSelectedListener { item ->
            floatingBtn.setColorFilter(Color.argb(255, 74, 74, 74))

            selectFragment(item)
            true
        }

        //Behaviour when clicked on the map item
        floatingBtn.setOnClickListener {
            floatingBtn.setColorFilter(Color.argb(255, 237, 53, 37))

            //We set clicked on the none item in order to disable the rest of the items
            //but the fragment that is shown is the map fragment
            bottomBarView.menu.getItem(2).isChecked = true
            transitionWithModalAnimation(fragment = MapFragment(), useModalAnimation = false, addToBackStack = false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.supportFragmentManager.popBackStack()
        return true
    }

    private fun selectFragment(item: MenuItem) {

        val newFragment: BaseFragment

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
            R.id.schedule_menu_id -> newFragment = ScheduleFragment.newInstance(scheduleModel = scheduleModel)
            R.id.none -> return
            else -> newFragment = MagicLineFragment()
        }
        transitionWithModalAnimation(fragment = newFragment, useModalAnimation = false, addToBackStack = false)
        currentFragment = newFragment
    }


}
