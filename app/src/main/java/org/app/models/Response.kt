package org.app.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

open class Response<T>(
    val result: T? = null,
    val code: String = ""
)

open class ProductDetailResponse<T>(
    val product: T? = null
)

@JsonClass(generateAdapter = true)
open class PagingResponse<T>(
    @Json(name = "products") val products: List<T>?
)
