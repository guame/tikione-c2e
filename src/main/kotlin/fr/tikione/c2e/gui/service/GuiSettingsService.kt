package fr.tikione.c2e.gui.service

import com.thoughtworks.xstream.XStream
import fr.tikione.c2e.core.model.config.C2EConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.FileWriter


/**
 * @author Guillaume
 * 16/11/2017 - 21:51
 * 
 * I have tried to use the Preferences API with a custom filesystem impl. 
 * But it is really complicated compared to a simple XML serialization operation and the generated configs pollutes the file system. 
 */
interface GuiSettingsService {
    fun save(config: C2EConfig)
    fun load(): C2EConfig
}

class GuiSettingsServiceImpl : GuiSettingsService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    //default home dir for the c2e dir. How to change ?
    val configDir = File(System.getProperty("user.home"), "c2e")
    val configFile = File(configDir, "c2e.conf.xml")
    val xstream = XStream()
    
    init {
        configDir.mkdirs()
    }
    
    override fun save(config: C2EConfig) {
        xstream.toXML(config, FileWriter(configFile))
    }

    override fun load(): C2EConfig {
        if(configFile.exists()) {
            try {
                return xstream.fromXML(FileReader(configFile)) as C2EConfig
            } catch (e: Exception) {
                log.error("Impossible de lire la configuration dans: {}", configFile.path, e)
                return C2EConfig()
            }
        }
        else 
            return C2EConfig()         
    }
    
}