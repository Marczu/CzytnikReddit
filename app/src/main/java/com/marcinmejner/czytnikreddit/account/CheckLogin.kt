package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckLogin(   @SerializedName("json")
                    @Expose
                    var json: Json? = null) {

}