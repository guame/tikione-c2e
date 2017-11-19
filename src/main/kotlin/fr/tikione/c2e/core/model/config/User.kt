package fr.tikione.c2e.core.model.config

import javax.xml.bind.annotation.XmlElement

/**
 * @author Guillaume
 * 16/11/2017 - 21:44
 */

data class User (
        @XmlElement var username : String = "",
        @XmlElement var password : String = ""
) {
    constructor() : this(username="", password = "")
}