package fr.tikione.c2e.gui.service

import fr.tikione.c2e.core.model.config.CpcScrapperConfig
import fr.tikione.c2e.gui.CpcScrapperGui
import java.io.File

import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.namespace.QName


/**
 * @author Guillaume
 * 16/11/2017 - 21:51
 * 
 * I have tried to use the Preferences API with a custom filesystem impl. 
 * But it is really complicated compared to a simple XML serialization operation and the generated configs pollutes the file system. 
 */
interface GuiSettingsService {
    fun save(config: CpcScrapperConfig)
    fun load(): CpcScrapperConfig
}

class GuiSettingsServiceImpl : GuiSettingsService {
    val configFile = File(System.getProperty("user.home"), ".cpcscrapper.jaxb.xml")
    public val configFileII = File(System.getProperty("user.home"), ".cpcscrapper.xstream.xml")
    
    val jaxbContext = JAXBContext.newInstance(CpcScrapperConfig::class.java)
    val marshaller = jaxbContext.createMarshaller()
    val unmarshaller = jaxbContext.createUnmarshaller()
    val name = QName(null, "cpcScrapperConfig")
    
    init {
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        if(!configFile.parentFile.exists())
            configFile.parentFile.mkdirs()
    }
    
    override fun save(config: CpcScrapperConfig) {
        val jaxbElement = JAXBElement<CpcScrapperConfig>(name, CpcScrapperConfig::class.java, config)
        marshaller.marshal(jaxbElement, configFile)
    }

    override fun load(): CpcScrapperConfig {
        try {
            return unmarshaller.unmarshal(configFile) as CpcScrapperConfig
        } catch (e: Exception) {
            e.printStackTrace()
            return CpcScrapperConfig()
        }
    }
    
}