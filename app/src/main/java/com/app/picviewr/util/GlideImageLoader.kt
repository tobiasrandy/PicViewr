package com.app.picviewr.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GlideImageLoader(private val context: Context) {

    suspend fun loadImage(
        imageUrl: String?,
        imageView: ImageView,
        placeholderResId: Int,
        errorResId: Int
    ) {
        withContext(Dispatchers.IO) {
            try {
                Glide.with(context)
                    .load(imageUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(placeholderResId)
                            .error(errorResId)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(imageView)
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(errorResId)
                }
            }
        }
    }
}