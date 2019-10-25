package org.app.api

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.app.demo.android.BuildConfig
import org.app.common.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RestAPI {
    companion object {

        var apiInterface: ApiInterface? = null

        fun getInstance() : ApiInterface {
            if (apiInterface != null) return apiInterface as ApiInterface

            val retrofit = getRetrofit(BuildConfig.BASE_URL_SERVER, true)

            apiInterface = retrofit.create(ApiInterface::class.java)
            return apiInterface as ApiInterface
        }

        private fun getRetrofit(baseUrl: String, auth: Boolean? = false): Retrofit{
            val logging = HttpLoggingInterceptor()
            logging.apply {
                this.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
            val client = OkHttpClient.Builder()
                .followRedirects(true)
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder().url(chain.request().url.toString())
                    requestBuilder
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("X-Requested-With", "XMLHttpRequest")

                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response
                }
                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}

/**
 * Asynchronously subscribes subscribers to this Single and emit its item on main thread
 */
fun <T> Single<T>.subscribeOnMain(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
