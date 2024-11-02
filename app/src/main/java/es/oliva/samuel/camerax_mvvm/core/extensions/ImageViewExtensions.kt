package es.oliva.samuel.camerax_mvvm.core.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.fromUri(uri: Uri) {
    Glide.with(this.context).load(uri).into(this)
}

fun ImageView.fromUriScaleDown(uri: Uri, targetSize: Int) {
    Glide.with(context).load(uri).apply(
        RequestOptions().override(targetSize).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
    ).into(this)
}
