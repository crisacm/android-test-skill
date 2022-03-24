package com.github.crisacm.testskills.data.network.models

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("name") var name: String = "",
    @SerializedName("username") var username: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("address") var address: Address? = null,
    @SerializedName("phone") var phone: String = "",
    @SerializedName("website") var website: String = "",
    @SerializedName("company") var company: Company? = null
) {
    data class Address(
        @SerializedName("street") var street: String = "",
        @SerializedName("suite") var suite: String = "",
        @SerializedName("city") var city: String = "",
        @SerializedName("zipcode") var zipcode: String = "",
        @SerializedName("geo") var geo: Geo? = null
    ) {
        data class Geo(
            @SerializedName("lat") var lat: Double = 0.0,
            @SerializedName("lng") var lng: Double = 0.0
        )
    }

    data class Company(
        @SerializedName("name") var name: String = "",
        @SerializedName("catchPhrase") var catchPhrase: String = "",
        @SerializedName("bs") var bs: String = ""
    )
}