package com.example.senamit.stationaryhutpro.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserAddress(var fullName: String, var mobileNumber: String, var pinCode: String, var addressPartOne: String,
                  var addressPartTwo: String, var landMark: String, var city: String, var state: String, var status: String) {

    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("fullName", fullName)
        result.put("mobileNumber", mobileNumber)
        result.put("pinCode", pinCode)
        result.put("addressPartOne", addressPartOne)
        result.put("addressPartTwo", addressPartTwo)
        result.put("landMark", landMark)
        result.put("city", city)
        result.put("state", state)
        result.put("status", status)

        return result
    }

//    private var fullName: String? = null
//    private var mobileNumber: String? = null
//    private val pinCode: String? = null
//    private val addressPartOne: String? = null
//    private val addressPartTwo: String? = null
//    private val landMark: String? = null
//    private val city: String? = null
//    private val state: String? = null
//    private val currentAddressStatus: String? = null


}
