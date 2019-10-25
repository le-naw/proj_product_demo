package org.app.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductPrice(
    @Json(name = "supplierSalePrice") val supplierSalePrice: String?,
    @Json(name = "sellPrice") val sellPrice: String?
)
