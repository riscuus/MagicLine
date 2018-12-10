package com.voluntariat.android.magicline.activities.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.voluntariat.android.magicline.R
import com.voluntariat.android.magicline.R.drawable.*
import com.voluntariat.android.magicline.models.MoreInfoMLModel
import kotlinx.android.synthetic.main.toolbar_appbar_top.*
import kotlinx.android.synthetic.main.toolbar_appbar_top.view.*
import com.voluntariat.android.magicline.R.string.*
import kotlinx.android.synthetic.main.fragment_more_info_ml.view.*
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.voluntariat.android.magicline.R.color.*
import kotlinx.android.synthetic.main.fragment_more_info_ml.*

class MoreInfoMLFragment : Fragment() {

    private lateinit var moreInfoMLView: View
    private lateinit var moreInfoMLDataModel: MoreInfoMLModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        moreInfoMLView = inflater.inflate(R.layout.fragment_more_info_ml, container, false)
        initToolbar()
        return moreInfoMLView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        createBarChart()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)
        moreInfoMLView.topToolbar.title = getString(R.string.ml)
        moreInfoMLView.topToolbar.navigationIcon = ContextCompat.getDrawable(context, ic_black_cross)
        moreInfoMLView.topToolbar.setNavigationOnClickListener { activity.onBackPressed() }

        val text: String = getString(walk_text_1) + " "
        val text2: String =  "<b>"+ getString(walk_text_2)+ "</b>" + " "
        val text3: String = getString(walk_text_3) + " "
        val text4: String = "<b>"+ getString(walk_text_4) +"</b>"
        val textView = moreInfoMLView.firstWalkText
        textView.text = Html.fromHtml(text + text2 + text3 + text4)
    }

    private fun createBarChart() {
        setupBarChartData()
        setUpPieChartData()
    }

    private fun setUpPieChartData() {

        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(20f))
        yVals.add(PieEntry(80f))


        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=0f
        val colors = java.util.ArrayList<Int>()
        colors.add(activity.getColor(light_red))
        colors.add(activity.getColor(mesque_background))

        dataSet.colors = colors
        pieChart.animateY(2000)
        val data = PieData(dataSet)
        pieChart.data = data
        dataSet.sliceSpace = 2f

        pieChart.holeRadius = 80f
        pieChart.centerText = "13.000" + "\n" + getString(R.string.vacancy)
        pieChart.setCenterTextSize(25.0f)
        var typeFace: Typeface? = ResourcesCompat.getFont(context, R.font.lato_light)
        pieChart.setCenterTextTypeface(typeFace)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

    }

    private fun setupBarChartData() {

    }


}
