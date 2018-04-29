package com.marcinmejner.czytnikreddit.utils

import android.util.Log

class ExtractXML(
        val xml: String,
        val tag: String,
        var endtag: String = "NONE"
        ) {

    private val TAG = "ExtractXML"



    fun start(): ArrayList<String>{
        val result = ArrayList<String>()
        var splitXml : Array<String>
        val marker: String

        if(endtag.equals("NONE")){
            marker = "\""
            splitXml = xml!!.split(tag!! + marker).toTypedArray()
        }else{
            marker = endtag
            splitXml = xml.split(tag).toTypedArray()
        }

//        var splitXml = xml!!.split((tag!! + "\"").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        Log.d(TAG, "start: $splitXml")
        val count = splitXml.size
        Log.d(TAG, "start: splitXml.size to : $count ")

        for(i in 1 until count){
            var temp = splitXml[i]
            val index = temp.indexOf(marker)
            Log.d(TAG, "start: indeks to : $index")
            Log.d(TAG, "start: ekstracted : $temp")

            temp = temp.substring(0, index)
            Log.d(TAG, "start: Snipped : $temp")
            result.add(temp)
        }

        return result
    }

}