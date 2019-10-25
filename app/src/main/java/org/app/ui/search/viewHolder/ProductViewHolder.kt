package org.app.ui.search.viewHolder

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import org.app.demo.android.databinding.ItemProductBinding
import org.app.models.Product
import org.app.ui.search.adapter.ProductAdapter

class ProductViewHolder(val binding: ItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var latestClickTime: Long = 0

    fun bindTo(position: Int, item: Product?, listener: ProductAdapter.OnItemClickListener?) {
        item?.let {
            binding.product = item
            binding.image = ""
            item.images?.let {
                if (it.isNotEmpty()) {
                    it.get(0).let {
                        binding.image = it.url
                    }
                }
            }
            binding.itemProroduct.setOnClickListener {
                if (checkClicked()) return@setOnClickListener
                listener?.onClick(position, item)
            }
        }
    }

    private fun checkClicked(): Boolean {
        if (SystemClock.elapsedRealtime() - latestClickTime < 600) return true
        latestClickTime = SystemClock.elapsedRealtime()
        return false
    }
}
