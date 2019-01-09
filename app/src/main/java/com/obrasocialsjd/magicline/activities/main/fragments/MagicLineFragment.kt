package com.obrasocialsjd.magicline.activities.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.obrasocialsjd.magicline.R
import com.obrasocialsjd.magicline.activities.main.adapters.NewsAdapter
import com.obrasocialsjd.magicline.activities.main.general.MainActivity
import com.obrasocialsjd.magicline.activities.main.otherui.CirclePagerIndicatorDecoration
import com.obrasocialsjd.magicline.activities.main.otherui.RrssView
import com.obrasocialsjd.magicline.data.MagicLineRepositoryImpl
import com.obrasocialsjd.magicline.data.models.donations.DonationsDBModel
import com.obrasocialsjd.magicline.data.models.posts.PostsItem
import com.obrasocialsjd.magicline.db.MagicLineDB
import com.obrasocialsjd.magicline.models.DetailModel
import com.obrasocialsjd.magicline.models.NewsModel
import com.obrasocialsjd.magicline.utils.*
import com.obrasocialsjd.magicline.viewModel.MagicLineViewModel
import com.obrasocialsjd.magicline.viewModel.MagicLineViewModelFactory
import kotlinx.android.synthetic.main.fragment_magic_line.rrssView
import kotlinx.android.synthetic.main.layout_a_fons.*
import kotlinx.android.synthetic.main.layout_countdown_bottom.*
import kotlinx.android.synthetic.main.layout_countdown_top.*
import kotlinx.android.synthetic.main.layout_mes_que.*
import kotlinx.android.synthetic.main.layout_news.*
import kotlinx.android.synthetic.main.layout_rrss.*
import kotlinx.android.synthetic.main.layout_recaudats_participants.*
import java.text.SimpleDateFormat
import java.util.*

class MagicLineFragment : BaseFragment() {

    private lateinit var mMagicLineViewModel: MagicLineViewModel
    private lateinit var myNewsAdapter: NewsAdapter

    //Programming section widgets
    lateinit var progRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MagicLineRepositoryImpl(MagicLineDB.getDatabase(requireActivity().applicationContext))
        val factory = MagicLineViewModelFactory(requireActivity().application, repository)
        mMagicLineViewModel = ViewModelProviders.of(this, factory).get(MagicLineViewModel::class.java)
    }
    //Setting the corresponding view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_magic_line, container, false)
    }

    override fun onStart() {
        super.onStart()

        //Init TextViews, etc
        val txtArray = initWidgets()

        initCountDown(txtArray)

        initStaticContent()

        initMoreInfoMLListener()

        initNewsRecycler()

        initMesQueListeners()

        initAfonsListeners()

        initRrss()
    }

    private fun initRrss() {
        rrssView.fbListener = { TODO() }
        rrssView.instaListener = { TODO() }
        rrssView.twitterListener = { TODO() }
    }

    private fun initMesQueListeners() {

        btnDonateTeam.setOnClickListener {
            (activity as AppCompatActivity).transitionWithModalAnimation(DonationsFragment.newInstance())
        }

        btnRequestDonates.setOnClickListener {
            (activity as AppCompatActivity).transitionWithModalAnimation(InviteFriendsFragment.newInstance())
        }

        btnBrainStorm.setOnClickListener {
            if (activity is MainActivity) (activity as MainActivity).callIntent(URL_IDEAS_GUIDE)
        }
    }

    private fun initMoreInfoMLListener() {
        moreInfoML.setOnClickListener {
            (activity as AppCompatActivity).transitionWithModalAnimation(fragment = MoreInfoMLFragment.newInstance(), showShareView = true)
        }
    }

    private fun initStaticContent() {
        participants_num.text = getString(R.string.cityParticipants).addThousandsSeparator()
    }

    private fun initWidgets(): Array<TextView> {
        return arrayOf(countdownDays, countdown_hores, countdownMin, countdownSec)
    }

    private fun initCountDown(txtDies: Array<TextView>) {
        val currentTime: Long = Calendar.getInstance().timeInMillis
        val dateLong: Long = resources.getString(R.string.magicLineDate).toLong()
        val diff: Long = dateLong - currentTime
        MyCounter(diff, 1000, txtDies).start()
    }

    private fun initNewsRecycler() {
        val myNewsManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        myNewsAdapter = NewsAdapter(ArrayList())

        newsRecyclerView.layoutManager = myNewsManager
        newsRecyclerView.adapter = myNewsAdapter
        newsRecyclerView.setPadding(0,0,0,50)

        //Adding pager behaviour
        val snapHelper = PagerSnapHelper()
        newsRecyclerView.onFlingListener = null //<-- We add this line to avoid the app crashing when returning from the background
        snapHelper.attachToRecyclerView(newsRecyclerView)
        newsRecyclerView.addItemDecoration(CirclePagerIndicatorDecoration())

        //Adding buttons listeners
        initArrowsListeners(myNewsManager)

        subscribeToPosts()
    }

    private fun subscribeToPosts() {
        mMagicLineViewModel.getPosts(getAPILang(requireContext())).observe(this, androidx.lifecycle.Observer {
            myNewsAdapter.loadItems(toNewsModel(it))
            myNewsAdapter.notifyDataSetChanged()})

        mMagicLineViewModel.getDonations().observe(this, androidx.lifecycle.Observer { donation ->
            if (donation != null) {
                recaudats_num.text = getDonationsByCity(donation).addCurrency()
            }
        })
    }

    private fun toNewsModel(list: List<PostsItem>): List<NewsModel> {
        val news : MutableList<NewsModel> = mutableListOf()
        for (item in list) {
            news.add(NewsModel(title = item.post.title, subtitle = item.post.teaser, description = item.post.text))
        }
        return news
    }

    private fun initArrowsListeners(mLayoutManager: LinearLayoutManager) {

        right_arrow_relative.setOnClickListener {
            val totalItemCount = newsRecyclerView.adapter?.itemCount ?: 0

            if (totalItemCount < 0) return@setOnClickListener

            val lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition()

            if (lastVisibleItemIndex >= totalItemCount) return@setOnClickListener

            mLayoutManager.smoothScrollToPosition(newsRecyclerView, null, lastVisibleItemIndex + 1)
        }

        left_arrow_relative.setOnClickListener {
            val totalItemCount = newsRecyclerView.adapter?.itemCount ?:0

            if (totalItemCount < 0) return@setOnClickListener

            val lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition()

            if (lastVisibleItemIndex <= 0) return@setOnClickListener

            mLayoutManager.smoothScrollToPosition(newsRecyclerView, null, lastVisibleItemIndex - 1)
        }
    }

    private fun initAfonsListeners() {

        val dataModelEssential = DetailModel(
                title = getString(R.string.essentials_title),
                subtitle = "",
                textBody = getString(R.string.essentials_body),
                link = getString(R.string.essentials_viewOnWeb),
                isBlack = true,
                toolbarImg = listOf(R.drawable.imprescindibles),
                hasToolbarImg = false,
                titleToolbar = getString(R.string.title_toolbar_imprs))
        val dataModelDestiny = DetailModel(
                title = getString(R.string.donations_title),
                subtitle = "",
                textBody = getString(R.string.donations_body),
                link = getString(R.string.donations_viewOnWeb),
                isBlack = true,
                toolbarImg = listOf(R.drawable.destidelfons, R.drawable.sliderimage2, R.drawable.sliderimage3, R.drawable.laboratori),
                hasToolbarImg = false)
        val dataModelSantJoan = DetailModel(
                title = getString(R.string.sjd_title),
                subtitle = getString(R.string.sjd_subtitle),
                textBody = getString(R.string.sjd_body),
                link = getString(R.string.sjd_viewOnWeb),
                isBlack = true,
                toolbarImg = listOf(R.drawable.sliderimage2, R.drawable.sliderimage3, R.drawable.laboratori, R.drawable.destidelfons),
                hasToolbarImg = false)

        info_essentials_button.setOnClickListener {
            (activity as AppCompatActivity).transitionWithModalAnimation(DetailFragment.newInstance(dataModelEssential))
        }

        info_donations_destiny_button.setOnClickListener {
            (activity as AppCompatActivity).transitionWithModalAnimation(DetailFragment.newInstance(dataModelDestiny))
        }

        info_sjd_button.setOnClickListener {
            (activity as AppCompatActivity).transitionWithModalAnimation(DetailFragment.newInstance(dataModelSantJoan))
        }
    }

    private fun getDonationsByCity(donation : DonationsDBModel) : Double {
        return when (getFlavor()) {
            BARCELONA -> donation.donationsBcn!!.toDouble()
            MALLORCA -> donation.donationsMll!!.toDouble()
            VALENCIA -> donation.donationsVal!!.toDouble()
            else -> 0.0
        }
    }
}
