package com.javlonrahimov1212.apod12.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.javlonrahimov1212.apod12.R
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.utils.getThumbnailUrl
import kotlinx.android.synthetic.main.item_apod_gallery.view.*


class ApodGalleryAdapter() :
    RecyclerView.Adapter<ApodGalleryAdapter.ApodGalleryViewHolder>() {

    var onItemClickedGalleryAdapter: OnItemClickedGalleryAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodGalleryViewHolder {
        return ApodGalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_apod_gallery, parent, false),
            onItemClickedGalleryAdapter
        )
    }

    private var apods: List<Apod> = ArrayList()

    override fun getItemCount(): Int {
        return apods.size
    }

    override fun onBindViewHolder(holder: ApodGalleryViewHolder, position: Int) {
        holder.bindData(apods[position], position)
    }

    fun submitList(apods: List<Apod>) {

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ApodGalleryAdapterDiffCallback(
                this.apods,
                apods
            )
        )
        this.apods = apods
        diffResult.dispatchUpdatesTo(this)
    }

    fun submitListFav(apods: List<Apod>) {
        this.apods = apods
        notifyDataSetChanged()
    }

    class ApodGalleryViewHolder(
        itemView: View,
        var onItemClickedGalleryAdapter: OnItemClickedGalleryAdapter?
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val apodImage: ImageView = itemView.image_item_apod
        private val apodTitle: TextView = itemView.title_item_apod
        private val apodDescription: TextView = itemView.description_item_apod
        private val apodCopyright: TextView = itemView.copyright_item_apod

        fun bindData(apod: Apod, position: Int) {
            when {
                apod.url.endsWith(".jpg") -> {
                    Glide.with(apodImage.context)
                        .load(apod.url)
                        .into(apodImage)
                }
                apod.url.contains("youtube") -> {
                    Glide.with(apodImage.context)
                        .load(getThumbnailUrl(apod.url))
                        .into(apodImage)
                }
                else -> {
                    apodImage.setImageResource(R.drawable.placeholder)
                }
            }
            apodTitle.text = apod.title
            apodDescription.text = apod.explanation
            apodCopyright.text = apod.copyright
            apodCopyright.isSelected = true

            if (apod.isLiked) {
                itemView.fav_button_item_apod_gallery.setImageResource(R.drawable.ic_round_favorite_24)
            } else {
                itemView.fav_button_item_apod_gallery.setImageResource(R.drawable.ic_round_favorite_border_24)
            }

            itemView.setOnClickListener {
                onItemClickedGalleryAdapter?.onClick(apod.date)
            }
            itemView.fav_button_item_apod_gallery.setOnClickListener {
                onItemClickedGalleryAdapter?.onFavButtonClicked(apod)
            }

        }
    }
}

interface OnItemClickedGalleryAdapter {

    fun onClick(date: String)

    fun onFavButtonClicked(apod: Apod)
}

class ApodGalleryAdapterDiffCallback(var oldList: List<Apod>, var newList: List<Apod>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == (newList[newItemPosition])
    }
}