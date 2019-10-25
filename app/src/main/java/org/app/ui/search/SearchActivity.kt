package org.app.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.rvSakeList
import org.app.common.Constants
import org.app.demo.android.R
import org.app.common.PagingState
import org.app.demo.android.databinding.ActivitySearchBinding
import org.app.extensions.getActivity
import org.app.extensions.showDialogNetworkError
import org.app.models.Product
import org.app.ui.detail.DetailActivity
import org.app.ui.search.adapter.ProductAdapter

class SearchActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search) }
    private var pagingState: PagingState? = null
    private lateinit var adapterProduct: ProductAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root
        setupRecyclerView()
        listenPagingStateChange()
        initSearch()
        startSearch()
    }

    private fun setupRecyclerView() {
        binding.viewModel = viewModel
        adapterProduct = ProductAdapter(productItemClickListener)

        rvSakeList.apply {
            adapter = adapterProduct
            addOnScrollListener(retryListener)
        }
    }


    private val productItemClickListener = object : ProductAdapter.OnItemClickListener {
        override fun onClick(position: Int, product: Product) {
            val intent = DetailActivity.intent(getActivity())
            intent.putExtra(Constants.PRODUCT_ID, product.sku)
            startActivity(intent)
        }
    }

    // Retry when reached the last item in bottom of the recycler view
    private val retryListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (pagingState != PagingState.SUCCESS
                && dy > 0
                && mLayoutManager.childCount + mLayoutManager.findFirstVisibleItemPosition() >= mLayoutManager.itemCount
                && adapterProduct.getItemViewType(mLayoutManager.itemCount - 1) == ProductAdapter.ITEM_PAGING_LOADING_VIEW_TYPE
            ) viewModel.getRetry()
        }
    }

    private fun listenPagingStateChange() {
        var previousResultLoad = pagingState
        viewModel.pagingState.observe(this, Observer {
            pagingState = it
            adapterProduct.setPagingState(it)

            if (it == PagingState.FAILED && previousResultLoad != PagingState.FAILED) {
                showDialogNetworkError()
            }
            if (it == PagingState.FAILED || it == PagingState.SUCCESS) {
                previousResultLoad = it
            }
        })
    }

    private fun initSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (s.toString().isEmpty()) viewModel.resetCondition()
                    else viewModel.setKeyword(binding.edtSearch.text.toString())
                }
            }
        })

        binding.edtSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                startSearch()
                true
            }
            false
        }
    }

    private fun startSearch(){
        viewModel.updateConditionToSearch()
        viewModel.productPagedList.let {
            if (it.hasObservers()) return@let
            it.observe(this, Observer {
                adapterProduct.submitList(it)
            })
        }
    }

    companion object {
        fun intent(context: Context?) = Intent(context, SearchActivity::class.java)
    }
}
