package com.obrasocialsjd.magicline.activities.main.general

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.obrasocialsjd.magicline.R
import com.obrasocialsjd.magicline.activities.main.fragments.*
import com.obrasocialsjd.magicline.models.DetailModel
import com.obrasocialsjd.magicline.models.ScheduleCardModel
import com.obrasocialsjd.magicline.models.ScheduleGeneralModel
import com.obrasocialsjd.magicline.models.ScheduleTextModel
import com.obrasocialsjd.magicline.utils.transitionWithModalAnimation
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : BaseActivity() {

    private lateinit var currentFragment: Fragment
    private var scheduleModel: Array<ScheduleGeneralModel> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Prepare the mapFAB
        bottomBarFloatingButton.setColorFilter(ContextCompat.getColor(this, R.color.selected_indicator_color))
        bottomBarFloatingButton.supportBackgroundTintList = ContextCompat.getColorStateList(this, R.color.white)

        getData()

        initBottomBar()

        if (savedInstanceState == null) {
            transitionWithModalAnimation(fragment = MagicLineFragment(),
                    useModalAnimation = false, addToBackStack = false, showShareView = true)
            if (intent.hasExtra("From")) {
                navigateFromIntentExtra(intent.extras?.get("From") as Serializable?, false)
            }
        }

        initNavigation()
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

    fun initNavigation() {
        //Behaviour when clicked on a item different from map
        bottomBarView.setOnNavigationItemSelectedListener { item ->
            bottomBarFloatingButton.setColorFilter(ContextCompat.getColor(this, R.color.selected_indicator_color))
            bottomBarFloatingButton.supportBackgroundTintList = ContextCompat.getColorStateList(this, R.color.white)

            selectFragment(item)
            true
        }

        bottomBarFloatingButton.setOnClickListener {
            bottomBarFloatingButton.setColorFilter(ContextCompat.getColor(this, R.color.white))
            bottomBarFloatingButton.supportBackgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimary)

            //We set clicked on the none item in order to disable the rest of the items
            //but the fragment that is shown is the map fragment
            bottomBarView.menu.getItem(2).isChecked = true

            transitionWithModalAnimation(fragment = MapFragment(), useModalAnimation = false,
                    addToBackStack = false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.supportFragmentManager.popBackStack()
        return true
    }

    private fun selectFragment(item: MenuItem) {

        var showShareView = false

        if (!item.isChecked) {

            // Those are fragments of the main view, so we don't need the back-stack
            clearBackStack()

            val newFragment: BaseFragment
            when (item.itemId) {
                R.id.magicline_menu_id -> {
                    newFragment = MagicLineFragment()
                    showShareView = true
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
                R.id.schedule_menu_id -> newFragment = ScheduleFragment.newInstance(scheduleModel)
                R.id.none -> return
                else -> newFragment = MagicLineFragment()
            }

            transitionWithModalAnimation(fragment = newFragment, useModalAnimation = false,
                    addToBackStack = false, showShareView = showShareView)
            currentFragment = newFragment
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(Intent().apply { putExtra("From", "ferDonacio") })
        val extra = intent?.getSerializableExtra("From")
        navigateFromIntentExtra(extra)
    }
    private fun navigateFromIntentExtra(extra: Serializable?, openDefault: Boolean = true) {

        val dataModelEssential = DetailModel(
                title = getString(R.string.essentials_title),
                subtitle = getString(R.string.essentials_subtitle),
                textBody = getString(R.string.essentials_body),
                link = getString(R.string.essentials_viewOnWeb))

        when (extra) {
            "ultimaNoticia" -> this.transitionWithModalAnimation(DetailFragment.newInstance(dataModelEssential)) //navigation to the las news TODO: cambiar Fragment
            "ferDonacio" -> this.transitionWithModalAnimation(DonationsFragment())
            "detallsEsdeveniments" -> this.transitionWithModalAnimation(ScheduleFragment.newInstance(scheduleModel)) //Especificar qué Fragment
            else -> if (openDefault) this.transitionWithModalAnimation(MagicLineFragment())
        }
    }

    private fun getData() {
        scheduleModel = arrayOf(
                ScheduleTextModel("9:30", "Salida"),
                ScheduleCardModel("10:30", "Picnik", "Equipaments culturals obren les portes", getString(R.string.lorem_ipsum),
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

    private fun clearBackStack() {
        while (supportFragmentManager.backStackEntryCount != 0) {
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun callIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun manageBottomBar(isModal : Boolean) {

        if (isModal) {
            bottomBarView.visibility = View.GONE
            bottomBarFloatingButton.hide()
        } else {
            bottomBarView.visibility = View.VISIBLE
            bottomBarFloatingButton.show()
        }
    }

    fun manageShareView(hasShareView : Boolean) {

        val fragment = ShareFragment()
        val transaction = supportFragmentManager.beginTransaction()
        if (hasShareView) {
            transaction.add(R.id.frame_layout, fragment)
        } else {
            transaction.remove(fragment)
        }

        transaction.commit()
    }
}
