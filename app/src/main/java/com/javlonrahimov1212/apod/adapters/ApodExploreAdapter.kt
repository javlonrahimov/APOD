package com.javlonrahimov1212.apod.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.models.Photo
import kotlinx.android.synthetic.main.item_photo_explore_fragment.view.*

class ApodExploreAdapter(private val photos: List<Photo>) :
    RecyclerView.Adapter<ApodExploreAdapter.ApodExploreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodExploreViewHolder {
        return ApodExploreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_explore_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ApodExploreViewHolder, position: Int) {
        holder.bindData(photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class ApodExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val apodImage: ImageView = itemView.image_item_photo
        private val apodTitle: TextView = itemView.title_item_photo

        fun bindData(photo: Photo) {
            apodImage.setImageResource(
                photo.url
            )
            apodTitle.text = photo.title
        }

    }
}