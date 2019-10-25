package org.app.api

import io.reactivex.Single
import org.app.models.Response
import org.app.common.Constants
import org.app.models.*
import retrofit2.http.*

interface ApiInterface {

    /**
     * search product
     * @param _page Optional. The page index to load
     * @param q keyword search
     */
    @GET("/api/search")
    fun getProductSearch(
        @Query("_page") page: Long,
        @Query("q") q: String = "",
        @Query("channel") channel: String = "pv_showroom",
        @Query("visitorId") visitorId: String = "",
        @Query("terminal") terminal: String = "CP01",
        @Query("_limit") numSakePerPage: Int = Constants.NUMBER_OF_ITEMS_PER_PAGE
    ): Single<Response<PagingResponse<Product>>>

    /**
     * Get product detail by id
     */
    @GET("/api/products/{id}")
    fun getProductDetailById(
        @Path("id") id: String,
        @Query("channel") channel: String = "pv_showroom",
        @Query("terminal") terminal: String = "CP01"
    ): Single<Response<ProductDetailResponse<Product>>>

}
