package org.app.ui.search.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import org.app.api.RestAPI
import org.app.api.subscribeOnMain
import org.app.common.Constants
import org.app.common.PagingState
import org.app.models.Product
import java.util.concurrent.Executor

class PageKeyedProductSearchDataSource(private val condition: SearchCondition, private val retryExecutor: Executor) :
    PageKeyedDataSource<Long, Product>() {

    val pagingStateLiveData = MutableLiveData<PagingState>()
    private var retry: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Product>
    ) {
        pagingStateLiveData.postValue(PagingState.INITIAL_LOADING)
        val getData = RestAPI.getInstance().getProductSearch(page = 1, q = condition.keyword)
            .subscribeOnMain().subscribe(
                { response ->
                    pagingStateLiveData.postValue(PagingState.SUCCESS)
                    if (response.code == Constants.STATUS_OK) {
                        retry = null
                        val items = response.result?.products?.map { it } ?: emptyList()
                        callback.onResult(items, null, 2L)
                    } else {
                        retry = { loadInitial(params, callback) }
                    }
                },
                {
                    pagingStateLiveData.postValue(PagingState.FAILED)
                    retry = { loadInitial(params, callback) }
                }
            )
        CompositeDisposable().add(getData)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Product>) {
        pagingStateLiveData.postValue(PagingState.MORE_LOADING)
        val getData = RestAPI.getInstance().getProductSearch(page = params.key, q = condition.keyword)
            .subscribeOnMain().subscribe(
                { response ->
                    pagingStateLiveData.postValue(PagingState.SUCCESS)
                    if (response.code == Constants.STATUS_OK) {
                        retry = null
                        val items = response.result?.products?.map { it } ?: emptyList()
                        val nextKey = params.key + 1
                        callback.onResult(items, nextKey)
                    } else {
                        retry = { loadAfter(params, callback) }
                    }
                },
                {
                    pagingStateLiveData.postValue(PagingState.FAILED)
                    retry = { loadAfter(params, callback) }
                }
            )
        CompositeDisposable().add(getData)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Product>) {

    }

    fun retryFailedLoad() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }
}
