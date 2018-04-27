package com.marcinmejner.czytnikreddit.model

import com.marcinmejner.czytnikreddit.model.entry.Entry
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "feed", strict = false)
class Feed : Serializable {

    @set:Element(name = "icon")
    @get:Element(name = "icon")
    var icon: String? = null

    @set:Element(name = "id")
    @get:Element(name = "id")
    var id: String? = null

    @set:Element(required = false, name = "logo")
    @get:Element(required = false, name = "logo")
    var logo: String? = null

    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null

    @set:Element(name = "updated")
    @get:Element(name = "updated")
    var updated: String? = null

    @set:Element(name = "subtitle")
    @get:Element(name = "subtitle")
    var subtitle: String? = null

    @set:ElementList(inline = true, name = "entry")
    @get:ElementList(inline = true, name = "entry")
    var entrys: List<Entry>? = null

    override fun toString(): String {
        return "Feed: \n [Entrys: \n$]"
    }
}

