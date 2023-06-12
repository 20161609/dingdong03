package com.example.dingdong3

import android.content.Context
import android.graphics.LightingColorFilter
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

@GlideModule
class MyAppGlideModule : AppGlideModule()

fun getFileName(calendar: Calendar): String {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH).plus(1)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return (10000 * year + 100 * month + day).toString().plus(".jpeg")
}

fun cacheImageFromFirebase3(mainActivity: MainActivity ,context : Context, calendar : Calendar, imageView: ImageView) {
    try {
        val ref = Firebase.storage.reference.child(getFileName(calendar))
        val imageView : ImageView = mainActivity.findViewById(R.id.day_card)
        val mul = 0xFF8F8F8F
        val add = 0x00808080

        mainActivity.dayCardBox.addView(mainActivity.loadingAnimation)
        imageView.colorFilter = LightingColorFilter(mul.toInt(), add.toInt())
        ref.downloadUrl.addOnSuccessListener { uri ->
            // Preload the image
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .preload()

            // After preloading, load the image into the ImageView
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(imageView).run {
                    mainActivity.dayCardBox.removeView(mainActivity.loadingAnimation)
                    imageView.colorFilter = null
                }
            Log.e("getBytes", "Success")
        }.addOnFailureListener {
            // Handle any errors
            Log.e("getBytes", "failure")
        }
    } catch (e: Exception) {
        Log.e("Error", "Why??")
    }
}

fun cacheImageFromFirebase(mainActivity: MainActivity ,context : Context, calendar : Calendar, imageView: ImageView) {
    try {
        Log.e(getFileName(calendar).toString(), "filename")
        val ref = Firebase.storage.reference.child(getFileName(calendar))
        val imageView : ImageView = mainActivity.findViewById(R.id.day_card)
        val mul = 0xFF8F8F8F
        val add = 0x00808080
//        blurImage(mainActivity)
        mainActivity.dayCardBox.addView(mainActivity.loadingAnimation)
        imageView.colorFilter = LightingColorFilter(mul.toInt(), add.toInt())
        ref.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(getFileName(calendar))) // 사용자 지정 캐시 키를 설정합니다.
                .centerCrop()
                .into(imageView).run {
                    mainActivity.dayCardBox.removeView(mainActivity.loadingAnimation)
                    imageView.colorFilter = null
                }
            Log.e("getBytes", "Success")
        }.addOnFailureListener {
            // Handle any errors
            Log.e("getBytes", "failure")
        }
    } catch (e: Exception) {
        Log.e("Error", "Why??")
        println(e)
    }
}
// Display image from cache in ImageView
fun displayImageFromCache(imageView: ImageView, uri: Uri) {
    Glide.with(imageView.context)
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .centerCrop()
        .into(imageView)
}



