package com.example.desiregallery.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("login")
    var login: String = "",
    @SerializedName("birthday")
    var birthday: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("messageTokens")
    var messageTokens: List<String> = listOf()
) : Serializable
