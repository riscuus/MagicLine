package com.voluntariat.android.magicline.activities.main.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.voluntariat.android.magicline.R
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_donations.*




class DonationsFragment:Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_donations, container,  false)
    }

    override fun onStart() {

        super.onStart()

    }

}