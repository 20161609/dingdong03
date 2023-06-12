package com.example.dingdong3

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.util.*

fun buttonBlock2(mainActivity: MainActivity, context:Context){
    mainActivity.dialog.setOnShowListener {
        // dialog is shown
        buttonBlock = true
    }

    mainActivity.dialog.setOnDismissListener {
        // dialog is not shown
        buttonBlock = false
    }
}

fun getImageUri(context: Context, image: Bitmap): Uri {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val todayDate = year*10000 + month*100 + day

    val h = calendar.get(Calendar.HOUR_OF_DAY)
    val m = calendar.get(Calendar.MINUTE)
    val s = calendar.get(Calendar.SECOND)
    val totalSecond = m*60 + s
    val fileName : String = todayDate.toString() +
            (calendar.get(Calendar.MILLISECOND)+totalSecond).toString() +
            "_dingdong"


    val bytes = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, image, fileName, null)
    return Uri.parse(path)
}

fun button2Func(mainActivity: MainActivity, context: Context) {
    // block button & Create an instance of ActivityResultLauncher
    if(buttonBlock)return
    mCountDown.start()

    // Initialize the ImageView from the activity.
    val imageView: ImageView = mainActivity.findViewById(R.id.day_card)
    var bitmap: Bitmap? = null

    try {
        // Enable drawing cache on the image view to get the bitmap.
        imageView.isDrawingCacheEnabled = true
        // Create the bitmap from the image view's drawing cache.
        bitmap = Bitmap.createBitmap(imageView.drawingCache)
        // Disable the drawing cache as we don't need it anymore.
        imageView.isDrawingCacheEnabled = false

        // Get the URI of the bitmap.
        val uri = bitmap?.let { getImageUri(context, it) }

        // Create and configure the intent for image sharing.
        Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }.also { shareIntent ->
            // Start the share intent chooser.
            val chooser = Intent.createChooser(shareIntent,"Share Image")

            context.startActivity(Intent.createChooser(shareIntent,"Share Image"))
//            mainActivity.shareResultLauncher.launch(Intent.createChooser(shareIntent, "Share Image"))
        }

    } catch (exception: Exception) {
        println(exception)
        Log.e("????","What the fuck")
        // Handle or log any exception that may occur during the image sharing process.
    } finally {
        // Clean up the bitmap to avoid memory leaks.
        buttonBlock=false
        Log.e("FUCK YOU", "Baby")
        bitmap?.recycle()
    }
}
