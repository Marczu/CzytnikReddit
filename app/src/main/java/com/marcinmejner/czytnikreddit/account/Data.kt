package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("modhash") @Expose val modhash: String?,
        @SerializedName("cookie") @Expose val cookie: String?
)