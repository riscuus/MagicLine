package com.obrasocialsjd.magicline.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.obrasocialsjd.magicline.BuildConfig
import com.obrasocialsjd.magicline.R
import com.obrasocialsjd.magicline.activities.main.fragments.BaseFragment
import com.obrasocialsjd.magicline.activities.main.fragments.MagicLineFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

fun getFlavor() = BuildConfig.FLAVOR.capitalize()

fun <T> callback(success: ((Response<T>) -> Unit)?, failure: ((t: Throwable) -> Unit)? = null): Callback<T> {
    return object : Callback<T> {
        override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) { success?.invoke(response) }
        override fun onFailure(call: Call<T>, t: Throwable) { failure?.invoke(t) }
    }
}

fun AppCompatActivity.transitionWithModalAnimation(context: Context, fragment: BaseFragment, useModalAnimation: Boolean = true, addToBackStack: Boolean = true, analyticsScreen : TrackingUtils.Screens) {
    // Analytics
    TrackingUtils(context).track(analyticsScreen)
    val transaction = this.supportFragmentManager.beginTransaction()

    // Adds button bar management (show/hide) bundle's argument
    val bundle = fragment.arguments ?: Bundle()
    bundle.putBoolean(SHOW_BOTTOM_BAR_TAG, useModalAnimation)
    fragment.arguments = bundle

    if(useModalAnimation) transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_down, R.anim.slide_out_down)

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        if (useModalAnimation) transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_down, R.anim.slide_out_down)
    }

    if (useModalAnimation) {
        transaction.replace(R.id.frame_layout, fragment, IS_MODAL)
    }
    else {
        transaction.replace(R.id.frame_layout, fragment)
    }

    if(addToBackStack) transaction.addToBackStack(fragment.javaClass.canonicalName)
    transaction.commit()

    supportFragmentManager.executePendingTransactions()
}

fun Activity.openUrl(url: String) {9
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Activity.funNotAvailableDialog() {
    let {context ->
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.notAvailableText)
        builder.setNeutralButton(R.string.closeText){ _, _->}
        val dialog = builder.create()

        dialog.show()
    }
}

fun Activity.notAvailableDialog(title : Int= R.string.gps_not_available_title, message: Int = R.string.gps_not_available_text) {
    let {context ->
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNeutralButton(R.string.closeText){_,_->}
        val dialog = builder.create()
        dialog.show()
    }
}

fun String.htmlToSpanned() : Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun Double.addCurrency() : String {
    return this.addThousandsSeparator().plus(getCurrency())
}

fun String.addThousandsSeparator() : String {
    return this.toDouble().addThousandsSeparator()
}

fun Double.addThousandsSeparator() : String {
    return NumberFormat.getInstance(Locale.getDefault()).format(this)
}

fun shareApp(pkg: String, shareText: String = "", storeLink: String = ""): Intent {

    val waIntent = Intent(Intent.ACTION_SEND)
    waIntent.type = "text/plain"
    waIntent.putExtra(Intent.EXTRA_SUBJECT, "Magic Line")
    val text = "\n" +  shareText + "\n\n" + storeLink  + BuildConfig.APPLICATION_ID
    if (pkg.isNotEmpty()) waIntent.setPackage(pkg)
    waIntent.putExtra(Intent.EXTRA_TEXT, text)
    return waIntent
}

fun Activity.openShareActivity (intent : Intent) {
    startActivity(Intent.createChooser(intent, "Share with"))
}