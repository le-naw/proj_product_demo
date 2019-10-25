package org.app.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_product_image.view.*
import org.app.demo.android.R
import org.app.models.ProductImage

class ProductImageAdapter(var data: ArrayList<ProductImage>) : RecyclerView.Adapter<PagerVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH {
        return PagerVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product_image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        holder.itemView.run {
            if(data[position].url.toString().isNotEmpty())
                Glide
                    .with(this.context)
                    .load(data[position].url)
                    .apply(RequestOptions())
                    .error(R.drawable.img_product)
                    .into(ivProroductDetail)
        }
    }

}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
