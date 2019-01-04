package com.obrasocialsjd.magicline.activities.main.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.obrasocialsjd.magicline.R
import com.obrasocialsjd.magicline.R.drawable.*
import com.voluntariat.android.magicline.activities.main.adapters.SlideViewAdapter
import kotlinx.android.synthetic.main.fragment_detail.*
import com.obrasocialsjd.magicline.activities.main.otherui.CirclePagerIndicatorDecoration
import com.obrasocialsjd.magicline.models.DetailModel
import com.obrasocialsjd.magicline.utils.htmlToSpanned
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.layout_news.*
import kotlinx.android.synthetic.main.layout_share.view.*
import kotlinx.android.synthetic.main.toolbar_appbar_top.*
import kotlinx.android.synthetic.main.toolbar_appbar_top.view.*
import java.util.ArrayList

class DetailFragment : BaseFragment() {

    private lateinit var detailLayoutView: View
    private lateinit var detailModel: DetailModel
    private lateinit var myImagesAdapter: SlideViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailModel = arguments?.get("detailFragment") as DetailModel


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        detailLayoutView = inflater.inflate(R.layout.fragment_detail, container, false)
        initToolbar()
        initWidgets()
        return detailLayoutView
    }

    override fun onResume() {
        super.onResume()
        if (!detailModel.isBlack){
            detailLayoutView.topToolbar.navigationIcon?.setColorFilter(ContextCompat.getColor(this.requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP)
            detailLayoutView.topToolbar.setTitleTextColor(ContextCompat.getColor(this.requireContext(), R.color.white))
        } else {
            detailLayoutView.topToolbar.navigationIcon?.setColorFilter(ContextCompat.getColor(this.requireContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_ATOP)
            detailLayoutView.topToolbar.setTitleTextColor(ContextCompat.getColor(this.requireContext(), R.color.black))
        }

        if (detailModel.hasToolbarImg) {
            detailLayoutView.topToolbar.background= ContextCompat.getDrawable(this.requireContext(), detailModel.toolbarImg)

        }
        else{
            detailLayoutView.imgDetail.background = ContextCompat.getDrawable(this.requireContext(), detailModel.toolbarImg)

            Picasso
                    .get()
                    .load(detailModel.toolbarImg)
                    .resize(0,350)
                    .centerInside()
                    .into(imgDetail)
        }

        initImagesRecycler()
    }



    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)
        detailLayoutView.detailTitle.text = detailModel.title
        detailLayoutView.detailSubtitle.text = detailModel.subtitle
        detailLayoutView.detailBody.text = detailModel.textBody.htmlToSpanned()
        detailLayoutView.topToolbar.setNavigationIcon(ic_black_cross)
        detailLayoutView.topToolbar.title = detailModel.title
        detailLayoutView.topToolbar.setNavigationOnClickListener { this.requireActivity().onBackPressed()

        }
    }

    private fun initImagesRecycler() {
        val myImagesManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        myImagesAdapter = SlideViewAdapter(ArrayList())

        imagesRecyclerView.layoutManager = myImagesManager
        imagesRecyclerView.adapter = myImagesAdapter
        imagesRecyclerView.setPadding(0,0,0,50)

        //Adding pager behaviour
        val snapHelper = PagerSnapHelper()
        imagesRecyclerView.onFlingListener = null //<-- We add this line to avoid the app crashing when returning from the background
        snapHelper.attachToRecyclerView(newsRecyclerView)
        imagesRecyclerView.addItemDecoration(CirclePagerIndicatorDecoration())

        //fill the recyclerView with info
        val list: List<Int> = listOf(destidelfons, sliderimage2, sliderimage3, laboratori)
        val images : MutableList<ImageView> = mutableListOf()

        for(i in list){
         //  images[i].setImageDrawable().



        }

//        images.add(detailLayoutView.imgDetail.background = ContextCompat.getDrawable(this.requireContext(), detailModel.toolbarImg))
//        myImagesAdapter.loadItems(images)
//      (ContextCompat.getDrawable(this.requireContext(), images[0]))

    }

    private fun initWidgets() {

        detailLayoutView.viewOnWeb.setOnClickListener {
            openNewTabWindow(detailModel.link, this.requireContext())
        }
        detailLayoutView.fb_button.setOnClickListener {
            openNewTabWindow(detailModel.link, this.requireContext())
        }
        detailLayoutView.insta_button.setOnClickListener {
            openNewTabWindow(detailModel.link, this.requireContext())
        }
        detailLayoutView.twitter_button.setOnClickListener {
            openNewTabWindow(detailModel.link, this.requireContext())
        }
    }

    private fun openNewTabWindow(url: String, context: Context) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    companion object {
        fun newInstance(dataModel: DetailModel): BaseFragment {
            val myFragment = DetailFragment()
            val args = Bundle()
            args.putSerializable("detailFragment", dataModel)
            myFragment.arguments = args
            return myFragment
        }
    }
}


