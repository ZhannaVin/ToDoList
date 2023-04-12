package com.example.todolist.model_userdata

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    var email:String? = null,
    @SerializedName("password")
    var password: String? = null
)
