package com.app.picviewr.adapter

import Photo
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.picviewr.R
import com.app.picviewr.databinding.ItemPhotoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class PhotoAdapter(private val context: Context, private val photoList: ArrayList<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoHolder(binding)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val item = photoList[position]
        holder.bind(item)
    }

    inner class PhotoHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Photo) {
            Glide.with(context)
                .load(item.thumbnailUrl)
                .apply(
                    RequestOptions()
                        .error(R.drawable.image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.photo)

            binding.title.text = item.title
            binding.id.text = item.id.toString()
        }
    }
}