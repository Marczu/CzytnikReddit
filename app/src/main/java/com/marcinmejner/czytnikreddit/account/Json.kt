package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Json(
        @set: SerializedName("data")
        @get: SerializedName("data")
        @Expose
        var data: Data? = null)