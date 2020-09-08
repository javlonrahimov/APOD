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
import com.javlonrahimov1212.apod.utils.getThumbnailUrl
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_apod_gallery.view.*

class ApodGalleryAdapter(val apods: List<Apod>) :
    RecyclerView.Adapter<ApodGalleryAdapter.ApodGalleryViewHolder>() {

    var onItemClicked: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodGalleryViewHolder {
        return ApodGalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_apod_gallery, parent, false),
            onItemClicked
        )
    }

    override fun getItemCount(): Int {
        return apods.size
    }

    override fun onBindViewHolder(holder: ApodGalleryViewHolder, position: Int) {
        holder.bindData(apods[position])
    }

    class ApodGalleryViewHolder(
        itemView: View,
        var onItemClicked: OnItemClicked?
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val apodImage: ImageView = itemView.image_item_apod
        private val apodTitle: TextView = itemView.title_item_apod
        private val apodDescription: TextView = itemView.description_item_apod
        private val apodCopyright: TextView = itemView.copyright_item_apod

        fun bindData(apod: Apod) {
            if (apod.url.endsWith(".jpg"))
                Glide.with(apodImage.context)
                    .load(apod.url)
                    .into(apodImage)
            else {
                Glide.with(apodImage.context)
                    .load(getThumbnailUrl(apod.url))
                    .into(apodImage)
            }
            apodTitle.text = apod.title
            apodDescription.text = apod.explanation
            apodCopyright.text = apod.copyright
            itemView.setOnClickListener {
                onItemClicked?.onClick(apod.date)
            }

        }
    }
}

interface OnItemClicked {
    fun onClick(date: String)
}