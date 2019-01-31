package br.com.thingsproject.things.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Picasso
import java.lang.Exception

fun ImageView.loadUrl(url: String?, progress: ProgressBar? = null) {
    if (url == null || url.trim().isEmpty()) {
        setImageBitmap(null)
        return
    }
    if (progress == null) {
        Picasso.get().load(url).fit().into(this)
        Log.d("picasso", "carregamento pronto")
    } else {
        progress.visibility = View.VISIBLE
        Picasso.get().load(url).fit().into(this,
                object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //Download OK
                        progress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        progress.visibility = View.GONE
                    }
                })
    }
}