package com.example.weatherkotlinapp

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("description")
    var description: String? = null
}