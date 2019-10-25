package org.app.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(

    @Json(name = "sku") val sku: String,
    @Json(name = "name") val name: String?,
    @Json(name = "images") val images: List<ProductImage>?,
    @Json(name = "price") val price: ProductPrice?
) {
    var index: Int = -1
}
