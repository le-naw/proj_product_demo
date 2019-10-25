package org.app.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.app.common.PagingState
import org.app.demo.android.databinding.ItemProductBinding
import org.app.models.Product
import org.app.ui.search.viewHolder.LoadingStateItemViewHolder
import org.app.ui.search.viewHolder.ProductViewHolder

class ProductAdapter(val listener: OnItemClickListener) :
    PagedListAdapter<Product, RecyclerView.ViewHolder>(diffCallback) {
    private var pagingState: PagingState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE -> {
                val binding =
                    ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ProductViewHolder(binding)
            }
            ITEM_PAGING_LOADING_VIEW_TYPE -> LoadingStateItemViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            holder.bindTo(position, getItem(position), listener)
        }
        if (holder is LoadingStateItemViewHolder) {
            holder.bindTo(pagingState)
        }
    }

    override fun getItemViewType(position: Int) =
        if (hasExtraRow() && position == itemCount - 1) ITEM_PAGING_LOADING_VIEW_TYPE
        else ITEM_VIEW_TYPE

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0

    private fun hasExtraRow() = pagingState != null && pagingState != PagingState.SUCCESS

    fun setPagingState(newPagingState: PagingState) {
        val previousState = this.pagingState
        val hadExtraRow = hasExtraRow()
        this.pagingState = newPagingState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newPagingState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int, product: Product)
    }

    companion object {
        const val ITEM_VIEW_TYPE = 0
        const val ITEM_PAGING_LOADING_VIEW_TYPE = 1

        val diffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.sku == newItem.sku

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem

        }
    }
}
