package com.marcinmejner.czytnikreddit.model.entry

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "entry", strict = false)
data class Entry(
        @set:Element(name = "content")
        @get:Element(name = "content")
        var content: String? = null,

        @set:Element(required = false, name = "author")
        @get:Element(required = false, name = "author")
        var author: Author? = null,

        @set:Element(name = "id")
        @get:Element(name = "id")
        private var id: String? = null,

        @set:Element(name = "title")
        @get:Element(name = "title")
        var title: String? = null,

        @set:Element(name = "updated")
        @get:Element(name = "updated")
        var updated: String? = null
) : Serializable {


    override fun toString(): String {
        return "\n\nEntry{" +
                "content='" + content + '\''.toString() +
                ", author='" + author + '\''.toString() +
                ", id='" + id + '\''.toString() +
                ", title='" + title + '\''.toString() +
                ", updated='" + updated + '\''.toString() +
                '}'.toString() + "\n" +
                "--------------------------------------------------------------------------------------------------------------------- \n"
    }
}




