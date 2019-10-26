package org.app.ui.detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.app.common.Constants
import org.app.extensions.showDialogNetworkError
import org.app.models.Product
import org.app.api.RestAPI
import org.app.api.subscribeOnMain
import org.app.demo.android.databinding.ActivityDetailBinding

class DetailViewModel : ViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val productDetail: MutableLiveData<Product> = MutableLiveData()
    var numberPay: MutableLiveData<Int> = MutableLiveData(2)

    fun getProductDetailById(context: Context?, sakeId: String, binding : ActivityDetailBinding) {
        loading.value = true
        val getDetail = RestAPI.getInstance().getProductDetailById(sakeId)
            .subscribeOnMain().subscribe(
                { response ->
                    if (response.code == Constants.STATUS_OK) {
                        productDetail.postValue(response.result?.product)
                    }
                    loading.value = false
                },
                {
                    context?.showDialogNetworkError()
                    loading.value = false
                }
            )
        CompositeDisposable().add(getDetail)
    }
}
