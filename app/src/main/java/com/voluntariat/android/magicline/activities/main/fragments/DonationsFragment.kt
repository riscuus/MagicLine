package com.voluntariat.android.magicline.activities.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.voluntariat.android.magicline.R
import android.webkit.WebView
import android.graphics.Bitmap
import android.os.Build
import kotlinx.android.synthetic.main.fragment_donations.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings


class DonationsFragment: BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //To add a Script
        val css = "header, #results, body > div > h3 { display: none; }"
        val js = "var style = document.createElement('style'); style.innerHTML = '$css'; document.head.appendChild(style);"

     val v: View = inflater.inflate(R.layout.fragment_donations, container,  false)

        val settings = v.webviewDonation.settings
        settings.javaScriptEnabled = true //OJO

        v.webviewDonation.webViewClient = WebViewClient()
        v.webviewDonation.webChromeClient = WebChromeClient()


        // Enable zooming in web view
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true

        settings.domStorageEnabled = true

        //necessary to load images
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }

        // Set web view client
        v.webviewDonation.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
                v.progressBar.visibility = View.VISIBLE
                v.webviewDonation.evaluateJavascript(js, null)

                super.onPageStarted(view, url, favicon)

            }

            override fun onLoadResource(view: WebView?, url: String?) {
                v.webviewDonation.evaluateJavascript(js, null)

                v.progressBar?.visibility = View.VISIBLE
                super.onLoadResource(view, url)

            }

            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Enable disable back forward button
                v.progressBar.visibility = View.GONE
                v.webviewDonation.evaluateJavascript(js, null)

                v.progressBar.invalidate()
                super.onPageFinished(view, url)
            }
        }

        v.webviewDonation.loadUrl("https://www.magiclinesjd.org/ca/equips/")
        v.webviewDonation.clearCache(false)

        return v
        //testApi()

    }


    private fun testApi() {
        /*val loginModelClient = OkHttpClient().newBuilder()
                .addInterceptor(MagicLineInterceptor("acces_token"))
                .build()

        val magicLineService = retrofit.create(MagicLineService::class.java)


        val call = magicLineService.testAPI("Test")
        val result = call.execute().body()
        Log.e("API",result.toString())*/
    }

    companion object {
        fun newInstance() : BaseFragment {
            return DonationsFragment()
        }
    }



}