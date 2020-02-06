package com.urbanbase.udemy_mvvm.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name")
    val countryName: String?,
    @SerializedName("capital")
    val capital: String? = null,
    @SerializedName("flagPNG")
    val flagImgUrl: String? = null
)