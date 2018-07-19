package com.voluntariat.android.magicline.activities.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import java.util.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.voluntariat.android.magicline.*
import com.voluntariat.android.magicline.activities.main.adapters.CountdownPagerAdapter
import com.voluntariat.android.magicline.activities.main.adapters.NewsAdapter
import com.voluntariat.android.magicline.activities.main.adapters.ProgrammingAdapter
import com.voluntariat.android.magicline.models.NewsModel
import com.voluntariat.android.magicline.models.ProgrammingModel


class MagicLineFragment : Fragment() {

    //Countdown - recaudats widgets
    lateinit var viewPager: ViewPager
    lateinit var viewPagerIndicator: com.kingfisher.easyviewindicator.AnyViewIndicator

    //Programming section widgets
    lateinit var progRecyclerView: RecyclerView

    //News section widgets
    lateinit var newsRecyclerView: RecyclerView
    lateinit var leftArrowView: RelativeLayout
    lateinit var rightArrowView: RelativeLayout
    lateinit var recyclerViewIndicator: com.kingfisher.easyviewindicator.RecyclerViewIndicator


    /*
     * Setting the corresponding view
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_magic_line, container,  false)
    }

    /*
     * Once the view is ready we can initialize all components
     */

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

    }

    override fun onStart() {
        super.onStart()

        //Init TextViews, etc
        initWidgets()

        initCountdownPV(viewPager)

        initProgrammingCards()

        initNewsRecycler()
    }

    private fun initWidgets(){
        //Countdown - recaudats view pager
        viewPager = view!!.findViewById(R.id.principal_vp)
        viewPagerIndicator=view!!.findViewById(R.id.view_pager_indicator)

        //Programming cards
        progRecyclerView= view!!.findViewById(R.id.rv)

        //News
        newsRecyclerView=view!!.findViewById(R.id.news_recycler)
        leftArrowView=view!!.findViewById(R.id.left_arrow_relative)
        rightArrowView=view!!.findViewById(R.id.right_arrow_relative)
        recyclerViewIndicator=view!!.findViewById(R.id.news_pager_indicator)


    }

    private fun initCountdownPV(viewPager: ViewPager){
        /*
         * We use childFragmentManager instead of supportFragmentManager because
         * we are using a fragment inside a fragment
        */
        val adapter = CountdownPagerAdapter(childFragmentManager)

        viewPager.adapter=adapter

        //Number of fragments to scroll
        viewPagerIndicator.setItemCount(adapter.count)

        //Where does it start
        viewPagerIndicator.setCurrentPosition(0)

        //Change when scroll
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                viewPagerIndicator.setCurrentPosition(viewPager.currentItem)
            }

            override fun onPageSelected(position: Int) {
                // Check if this is the page you want.
            }
        })

    }

    private fun initProgrammingCards(){

        //data Test
        val events = ArrayList<ProgrammingModel>()
        events.add(ProgrammingModel("TOT EL DIA", "Museus Oberts"))
        events.add(ProgrammingModel("10:30", "Concert"))
        events.add(ProgrammingModel("Avui", "Dinar"))
        events.add(ProgrammingModel("Avui", "Dinar"))


        //Setting up the adapter and the layout manager for the recycler view
        progRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
        val adapter = ProgrammingAdapter(events)
        progRecyclerView.adapter = adapter
    }





    private fun initNewsRecycler() {
        val dataSet = arrayOf(NewsModel("Nou event en la programació", "In recent years people have realized the importance of proper diet and exercise, and recent surveys show that over the  otal ruta."), NewsModel("Segon event en la programació", "In recent years people have realized the importance of proper diet and exercise, and recent surveys show that over the  otal ruta."), NewsModel("Tercer event en la programació", "In recent years people have realized the importance of proper diet and exercise, and recent surveys show that over the  otal ruta."), NewsModel("Quart event en la programació", "In recent years people have realized the importance of proper diet and exercise, and recent surveys show that over the  otal ruta."))

        val myNewsManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val myNewsAdapter = NewsAdapter(dataSet)

        newsRecyclerView.layoutManager = myNewsManager
        newsRecyclerView.adapter = myNewsAdapter

        //Adding pager behaviour
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(newsRecyclerView)

        //Adding the page indicators
        recyclerViewIndicator.setRecyclerView(newsRecyclerView)

        //Adding buttons listeners
        initArrowsListeners(myNewsManager)
    }

    private fun initArrowsListeners(mLayoutManager: LinearLayoutManager){
        
        rightArrowView.setOnClickListener{
            val totalItemCount= newsRecyclerView.adapter.itemCount

            if(totalItemCount<0) return@setOnClickListener

            val lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition()

            if(lastVisibleItemIndex>=totalItemCount) return@setOnClickListener

            mLayoutManager.smoothScrollToPosition(newsRecyclerView, null, lastVisibleItemIndex+1)
        }
        leftArrowView.setOnClickListener {
            val totalItemCount= newsRecyclerView.adapter.itemCount

            if(totalItemCount<0) return@setOnClickListener

            val lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition()

            if(lastVisibleItemIndex<=0) return@setOnClickListener

            mLayoutManager.smoothScrollToPosition(newsRecyclerView, null, lastVisibleItemIndex-1)
        }
    }
}