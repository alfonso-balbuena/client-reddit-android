package com.alfonso.clientreddit.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.alfonso.clientreddit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import timber.log.Timber
import java.io.OutputStream
import java.util.*


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, url: String?) {
    url?.let {
        val imgUri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .asBitmap()
            .load(imgUri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imgUri.lastPathSegment?.let {
                        saveImage(imgView.context, resource, it)
                    }
                }
            })
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions().error(R.drawable.ic_mic_external_off_black_24dp))
            .into(imgView)
    }
}

@BindingAdapter("readImage")
fun bindBooleanImage(imgView: ImageView, isRead: Boolean) {
    val color = if (isRead) ContextCompat.getColor(imgView.context, R.color.read_image) else ContextCompat.getColor(
        imgView.context,
        R.color.no_read_image
    )
    imgView.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}

@BindingAdapter("showTime")
fun bindLongToString(txtView: TextView, date_utc: Long) {
    val millisecondsPerHour : Long = 1000 * 60 * 60
    val currentTime = Date()
    val utcTime = date_utc * 1000
    val diff = currentTime.time - utcTime
    val hour : Long = diff / millisecondsPerHour
    txtView.text = hour.toString() + " hours ago"
}

private fun saveImage(context: Context, image: Bitmap, name: String) {
    val resolver = context.contentResolver
    if(!existFile(resolver,"${name.substring(0, name.length - 5)}.jpeg")) {
        val output  = getOutput(resolver,name)
        if(!image.compress(Bitmap.CompressFormat.JPEG,95,output)) {
            Timber.d("Error saving bitmap")
        } else {
            Timber.d("Bitmap saved")
        }
        output?.close()
    }
}

private fun getOutput(resolver: ContentResolver, name: String) : OutputStream? {
    val contentValues = getContentValues(name)
    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    Timber.d("getResolverQ $imageUri ---- ${MediaStore.Images.Media.EXTERNAL_CONTENT_URI} ----- ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}" )
    return resolver.openOutputStream(Objects.requireNonNull(imageUri)!!)
}


private fun getContentValues(name : String) : ContentValues {
    val contentValues = ContentValues()
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "${name.substring(0, name.length - 5)}.jpeg")
    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    return contentValues
}

private fun existFile(resolver: ContentResolver,name: String) : Boolean {
    val projection = arrayOf(
        MediaStore.MediaColumns.DISPLAY_NAME
    )
    val query = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,null)
    query?.use {cursor ->
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        while (cursor.moveToNext()) {
            val displayName = cursor.getString(nameColumn)
            if(displayName == name)
                return true
        }
    }
    return false
}
