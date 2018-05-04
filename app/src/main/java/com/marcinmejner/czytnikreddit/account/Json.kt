package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Json(
        @SerializedName("data") @Expose val data: Data)







