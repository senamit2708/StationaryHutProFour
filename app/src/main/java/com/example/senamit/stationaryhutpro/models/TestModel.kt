package com.example.senamit.stationaryhutpro.models

import com.google.firebase.database.Exclude
import java.util.*

class TestModel(var productName: String, var productNumber: String, var productPrice: String, var imageUrl: String) {


    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["productNumber"] = productNumber
        result["productName"] = productName
        result["productPrice"] = productPrice
        result["imageUrl"] = imageUrl

        return result
    }
}
