package com.marcinmejner.czytnikreddit.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Json{

        @SerializedName("data")
        @Expose
        private var data: Data? = null



        override fun toString(): String {
                return "Json{" +
                        "data=" + data +
                        '}'.toString()
        }

}
