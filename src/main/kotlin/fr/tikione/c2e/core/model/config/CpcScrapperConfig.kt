package fr.tikione.c2e.core.model.config

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author Guillaume
 * 16/11/2017 - 22:20
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class CpcScrapperConfig(
        @XmlElement val user: User = User()  
) {
    constructor() : this(user=User())
}