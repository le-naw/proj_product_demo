package org.app.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat

@BindingAdapter("loadImageUrl")
fun ImageView.loadImageUrl(url: String?) {
    var imgURL = url
    if (url.isNullOrBlank() || url.isEmpty()) {
        imgURL = ""
    }

    Glide
        .with(this.context)
        .load(imgURL)
        .apply(RequestOptions())
        .error(org.app.demo.android.R.drawable.img_product)
        .into(this)
}

@BindingAdapter("textPrice")
fun TextView.textPrice(text: String?){
    if (text == null || text.isEmpty() || text.isBlank()) {
        setText("")
        visibility = View.GONE
    }
    else {
        val format = DecimalFormat( "#,###,###,##0" )
        val tx_new = format.format(text.toFloat()).toString().replace(",",".")
        setText(tx_new + "đ")
    }
}

@BindingAdapter("textPriceNotHidden")
fun TextView.textPriceNotHidden(text: String?){
    if (text == null || text.isEmpty() || text.isBlank()) setText("")
    else {
        val format = DecimalFormat( "#,###,###,##0" )
        val tx_new = format.format(text.toFloat()).toString().replace(",",".")
        setText(tx_new + "đ")
    }
}