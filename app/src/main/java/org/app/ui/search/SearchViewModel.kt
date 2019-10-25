package org.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.app.common.Constants
import org.app.models.Product
import org.app.ui.search.data.PagedListListing
import org.app.ui.search.data.ProductSearchDataSourceFactory
import org.app.ui.search.data.SearchCondition
import java.util.concurrent.Executors

class SearchViewModel : ViewModel() {

    val showResult: MutableLiveData<Boolean> = MutableLiveData(false)
    val showEmptyListView: MutableLiveData<Boolean> = MutableLiveData(false)

    private val condition: MutableLiveData<SearchCondition> = MutableLiveData(SearchCondition())
    val currentCondition: MutableLiveData<SearchCondition> = MutableLiveData(SearchCondition())

    private var dataSourceFactory =
        ProductSearchDataSourceFactory(SearchCondition(), Executors.newSingleThreadExecutor())

    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(Constants.NUMBER_OF_ITEMS_PER_PAGE)
        .setEnablePlaceholders(false)
        .setPrefetchDistance(1)
        .build()

    private val searchResult: LiveData<PagedListListing<Product>> =
        Transformations.map(condition) {
            dataSourceFactory =
                ProductSearchDataSourceFactory(it, Executors.newSingleThreadExecutor())
            val data = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                .setBoundaryCallback(object : PagedList.BoundaryCallback<Product>() {
                    override fun onZeroItemsLoaded() {
                        super.onZeroItemsLoaded()
                        showEmptyListView.value = true
                    }
                })
                .build()
            return@map PagedListListing<Product>(data,
                Transformations.switchMap(dataSourceFactory.sourceLiveData) { dataSource -> dataSource.pagingStateLiveData }
            ) { dataSourceFactory.sourceLiveData.value?.retryFailedLoad() }
        }

    val productPagedList = Transformations.switchMap(searchResult) { it.pagedList }

    val pagingState = Transformations.switchMap(searchResult) { it.pagingState }

    fun getRetry() = dataSourceFactory.sourceLiveData.value?.retryFailedLoad()

    fun setKeyword(keyword: String) {
        currentCondition.value = condition.value!!.copy(keyword = keyword)
    }

    fun resetCondition() {
        currentCondition.value = SearchCondition()
        showResult.value = false
        showEmptyListView.value = false
    }

    fun updateConditionToSearch() {
        condition.value = currentCondition.value
    }
}
