package org.app.ui.search.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_paging_loading.view.*
import org.app.demo.android.R
import org.app.common.PagingState

class LoadingStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    
    fun bindTo(isLoading: Boolean) {
        itemView.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    fun bindTo(pagingState: PagingState?) {
        itemView.loadingProgressBar.visibility =
            if (pagingState == PagingState.MORE_LOADING) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(parent: ViewGroup): LoadingStateItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_paging_loading,
                parent, false
            )
            return LoadingStateItemViewHolder(view)
        }
    }
}
