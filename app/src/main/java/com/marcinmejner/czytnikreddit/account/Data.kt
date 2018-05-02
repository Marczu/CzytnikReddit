package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data{

        @SerializedName("modhash")
        @Expose
        private var modhash: String? = null

        @SerializedName("cookie")
        @Expose
        private var cookie: String? = null


        override fun toString(): String {
                return "Data{" +
                        "modhash='" + modhash + '\''.toString() +
                        ", cookie='" + cookie + '\''.toString() +
                        '}'.toString()
        }

}