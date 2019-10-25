package org.app.ui.search.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.app.models.Product
import java.util.concurrent.Executor

class ProductSearchDataSourceFactory (private val condition: SearchCondition, private val retryExecutor: Executor) :
    DataSource.Factory<Long, Product>(){
    val sourceLiveData = MutableLiveData<PageKeyedProductSearchDataSource>()
    override fun create(): DataSource<Long, Product> {
        val source = PageKeyedProductSearchDataSource(condition, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
