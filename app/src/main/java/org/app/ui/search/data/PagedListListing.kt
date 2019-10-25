package org.app.ui.search.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.app.common.PagingState

data class PagedListListing<T> (
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val pagingState: LiveData<PagingState>,
    // retries any failed requests.
    val retry: () -> Unit
)
