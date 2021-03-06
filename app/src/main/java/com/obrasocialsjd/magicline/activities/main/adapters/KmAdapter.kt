package com.obrasocialsjd.magicline.activities.main.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.obrasocialsjd.magicline.R
import com.obrasocialsjd.magicline.utils.KM
import kotlinx.android.synthetic.main.km_cards.view.*

class KmAdapter (private val kmList: ArrayList<CardKm>,
                 private val onItemClick : ((Int) -> Unit), val context: Context) : RecyclerView.Adapter<KmAdapter.ViewHolder>() {

    var selectedPosition : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.km_cards, parent, false)
        cardView.TextViewMapTextKm.text = KM
        return ViewHolder(cardView, onItemClick)
    }

    override fun getItemCount(): Int {
        return kmList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val km : CardKm =  kmList[position]
        var colorBg : Int = ContextCompat.getColor(context, R.color.white)
        var colorTxt : Int = Color.parseColor("#80000000")
        var typeFace: Typeface? = ResourcesCompat.getFont(context, R.font.lato_light)

        if (selectedPosition == position) {
            colorBg = ContextCompat.getColor(holder.itemView.context, R.color.colorPrimary)
            colorTxt = ContextCompat.getColor(context, R.color.white)
            typeFace = ResourcesCompat.getFont(context, R.font.lato_bold)
        }

        holder.itemView.TextViewMapNumKm.setTextColor(colorTxt)
        holder.itemView.TextViewMapTextKm.setTextColor(colorTxt)
        holder.itemView.TextViewMapTextKm.typeface = typeFace
        holder.itemView.TextViewMapNumKm.typeface = typeFace
        holder.itemView.TextViewMapNumKm.text = km.numKm.toString()
        holder.itemView.CardRoute.setCardBackgroundColor(colorBg)
        holder.itemView.ExtraTxtView.setTextColor(colorTxt)
        holder.itemView.ExtraTxtView.typeface = typeFace
        holder.itemView.ExtraTxtView.text = km.extraTextKm
    }

    class ViewHolder(itemView: View, onItemClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{onItemClick.invoke(adapterPosition)}
        }

    }
}