package com.marcinmejner.czytnikreddit.comments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckComment(
        @SerializedName("success")
        @Expose
        val success: String
) {
}