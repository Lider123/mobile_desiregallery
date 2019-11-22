package com.example.desiregallery.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(val email: String, val password: String) : Serializable {
    @SerializedName("login")
    var login = ""
    @SerializedName("birthday")
    var birthday = ""
    @SerializedName("photo")
    var photo = ""
    @SerializedName("messageTokens")
    var messageTokens = listOf<String>()
}
