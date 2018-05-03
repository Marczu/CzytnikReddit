package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Json( @SerializedName("data")
                 @Expose
                 var data: Data? = null){





        override fun toString(): String {
                return "Json{" +
                        "data=" + data +
                        '}'.toString()
        }

}
