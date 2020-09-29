package com.javlonrahimov1212.apod.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.models.Apod
import kotlinx.android.synthetic.main.item_photo_explore_fragment.view.*

class ApodExploreAdapter() :
    RecyclerView.Adapter<ApodExploreAdapter.ApodExploreViewHolder>() {

    var apods = ArrayList<Apod>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickedExploreAdapter: OnItemClickedExploreAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodExploreViewHolder {
        return ApodExploreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_explore_fragment, parent, false),
            onItemClickedExploreAdapter
        )
    }

    override fun onBindViewHolder(holder: ApodExploreViewHolder, position: Int) {
        holder.bindData(apods[position])
    }

    override fun getItemCount(): Int {
        return apods.size
    }

    class ApodExploreViewHolder(
        itemView: View,
        var onItemClickedExploreAdapter: OnItemClickedExploreAdapter?
    ) : RecyclerView.ViewHolder(itemView) {
        private val apodImage: ImageView = itemView.image_item_photo
        private val apodTitle: TextView = itemView.title_item_photo

        fun bindData(apod: Apod) {

            Glide.with(apodImage.context)
                .load(apod.url)
                .placeholder(R.drawable.transparent)
                .into(apodImage)

            apodTitle.text = apod.title

            itemView.setOnClickListener {
                onItemClickedExploreAdapter?.onClick(apod)
            }
        }

    }
}

interface OnItemClickedExploreAdapter {
    fun onClick(apod: Apod)
}