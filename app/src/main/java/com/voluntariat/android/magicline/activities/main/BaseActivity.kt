package com.voluntariat.android.magicline.activities.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.voluntariat.android.magicline.utils.getPreferencesLanguage
import com.voluntariat.android.magicline.utils.updateBaseContextLocale

abstract class BaseActivity : AppCompatActivity() {

    /**
     *  Forcing a language locale based on user preferences
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.updateBaseContextLocale(getPreferencesLanguage(base)))
    }

    /**
     *  Infix function to start an activity without leaving the current one on the backstack
     */
    infix fun startAsRootActivity(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}