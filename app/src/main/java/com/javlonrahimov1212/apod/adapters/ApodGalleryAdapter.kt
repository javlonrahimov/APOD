package com.javlonrahimov1212.apod.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.models.Apod
import kotlinx.android.synthetic.main.item_apod_gallery.view.*

class ApodGalleryAdapter(val apods: List<Apod>) :
    RecyclerView.Adapter<ApodGalleryAdapter.ApodGalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodGalleryViewHolder {
        return ApodGalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_apod_gallery, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return apods.size
    }

    override fun onBindViewHolder(holder: ApodGalleryViewHolder, position: Int) {
        holder.bindData(apods[position])
    }

    class ApodGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val apodImage: ImageView = itemView.image_item_apod
        private val apodTitle: TextView = itemView.title_item_apod
        private val apodDescription: TextView = itemView.description_item_apod
        private val apodCopyright: TextView = itemView.copyright_item_apod

        fun bindData(apod: Apod) {
            apodImage.setImageResource(
                apod.imageUrl
            )
            apodTitle.text = apod.title
            apodDescription.text = apod.description
            apodCopyright.text = apod.copyright
        }

    }
}