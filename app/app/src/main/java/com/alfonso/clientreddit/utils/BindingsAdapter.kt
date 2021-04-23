package com.alfonso.clientreddit.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.alfonso.clientreddit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imgView : ImageView, url : String?) {
    url?.let {
        val imgUri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgUri)
            .apply(RequestOptions().error(R.drawable.ic_mic_external_off_black_24dp))
            .into(imgView)


    }
}

@BindingAdapter("readImage")
fun bindBooleanImage(imgView: ImageView, isRead : Boolean) {
    val color = if (isRead) ContextCompat.getColor(imgView.context, R.color.read_image) else ContextCompat.getColor(imgView.context, R.color.no_read_image)
    imgView.setColorFilter(color,android.graphics.PorterDuff.Mode.SRC_IN)
}

@BindingAdapter("showTime")
fun bindLongToString(txtView : TextView, date_utc : Long) {
    val millisecondsPerHour : Long = 1000 * 60 * 60
    val currentTime = Date()
    val utcTime = date_utc * 1000
    val diff = currentTime.time - utcTime
    val hour : Long = diff / millisecondsPerHour
    txtView.text = hour.toString() + " hours ago"
}