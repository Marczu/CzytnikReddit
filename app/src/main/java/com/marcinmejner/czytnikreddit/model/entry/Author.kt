package com.marcinmejner.czytnikreddit.model.entry

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "author", strict = false)
class Author : Serializable {

    @set:Element(name = "name")
    @get:Element(name = "name")
    var name: String? = null

    @set:Element(name = "uri")
    @get:Element(name = "uri")
    var uri: String? = null

    override fun toString(): String {
        return "Author{" +
                "name='" + name + '\''.toString() +
                ", uri='" + uri + '\''.toString() +
                '}'.toString()
    }
}