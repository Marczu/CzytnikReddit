package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @set: SerializedName("modhash")
        @get: SerializedName("modhash")
        @Expose
        var modhash: String? = "",

        @set: SerializedName("cookie")
        @get: SerializedName("cookie")
        @Expose
        var cookie: String? = ""
)