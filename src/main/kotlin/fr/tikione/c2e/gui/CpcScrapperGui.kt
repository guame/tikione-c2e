package fr.tikione.c2e.gui

import com.github.salomonbrys.kodein.instance
import com.thoughtworks.xstream.XStream
import fr.tikione.c2e.core.model.config.CpcScrapperConfig
import fr.tikione.c2e.core.model.config.CpcScrapperConfigII
import fr.tikione.c2e.core.model.config.User
import fr.tikione.c2e.gui.service.GuiSettingsService
import fr.tikione.c2e.gui.service.GuiSettingsServiceImpl
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.io.FileReader
import java.io.FileWriter
import fr.tikione.c2e.gui.kodein as kodein
/**
 * @author GUAM
 * 14.11.2017 - 13:44
 */


class CpcScrapperGui : Application() {

    private val root = BorderPane()
    
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {

        val gui = CpcScrapperGui()

        primaryStage.setOnCloseRequest({
            Platform.exit()
            System.exit(0)
        })

        gui.initUIPanels(primaryStage)
        
        primaryStage.show()
    }

    fun initUIPanels(stage: Stage) {
        stage.scene = Scene(root)

        stage.getIcons().add(Image(loadRessource("gui/troppuissant.png")))
        stage.title = "TikiOne C2E"
        root.resize(800.0, 600.0)
        root.center = Label("hello CPC !")
        val settings: GuiSettingsService = kodein.instance()
        val config: CpcScrapperConfig = kodein.instance()
        config.user.username = "William Vaurien"
        config.user.password = "aeapui!1"
        println(config)
        settings.save(config)
        val load = settings.load()
        println(load)
        
        
        XStream().toXML(CpcScrapperConfigII(user = User("toto", "pigeonvole")), FileWriter((settings as GuiSettingsServiceImpl).configFileII))
        val fromXML : CpcScrapperConfigII = XStream().fromXML(FileReader((settings as GuiSettingsServiceImpl).configFileII)) as CpcScrapperConfigII
        
        println(fromXML)
        
        
    }

    private fun loadRessource(path: String): String? {
        return Thread.currentThread().contextClassLoader.getResource(path).toExternalForm()
    }


}