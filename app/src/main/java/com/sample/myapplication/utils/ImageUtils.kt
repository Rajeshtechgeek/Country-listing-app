package com.sample.myapplication.utils

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.sample.myapplication.utils.svg.SvgSoftwareLayerSetter


object ImageUtils {

    fun loadSVGImage(context: Context, imgUrl: String?, imgView: ImageView, errorImg: Int,listener: RequestListener<PictureDrawable>){
        val requestBuilder: RequestBuilder<PictureDrawable> = Glide.with(context)
            .`as`(PictureDrawable::class.java)
            .error(errorImg)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter())
            .listener(listener)

        val uri: Uri = Uri.parse(imgUrl)
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.DATA).load(uri).into(imgView)

    }

    fun loadSVGImage(context: Context, imgUrl: String?, imgView: ImageView, errorImg: Int){
        val requestBuilder: RequestBuilder<PictureDrawable> = Glide.with(context)
            .`as`(PictureDrawable::class.java)
            .error(errorImg)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter())

        val uri: Uri = Uri.parse(imgUrl)
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.DATA).load(uri).into(imgView)
    }

    fun loadImageWithGlide(context: Context, imgUrl: String?, imgView: ImageView, errorImg: Int) {
        Log.d(TAG, "loadImageWithGlide#URL/URI :$imgUrl")
        if (imgUrl != null && !imgUrl.isEmpty()) {
            val options = RequestOptions()
                .placeholder(errorImg)
                .error(errorImg)
                .dontTransform()
                .dontAnimate()
                .transform(RoundedCorners(20))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(context).load(imgUrl).thumbnail(0.1f).apply(options).into(imgView)
        } else imgView.setImageResource(errorImg)
    }
}