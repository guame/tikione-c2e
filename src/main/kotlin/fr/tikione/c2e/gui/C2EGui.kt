package fr.tikione.c2e.gui

import com.github.salomonbrys.kodein.instance
import fr.tikione.c2e.core.model.config.C2EConfig
import fr.tikione.c2e.gui.service.GuiSettingsService
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

/**
 * @author GUAM
 * 14.11.2017 - 13:44
 */


class C2EGui : Application() {

    private val root = BorderPane()
    
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {

        val gui = C2EGui()

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
        val config: C2EConfig = kodein.instance()
        config.user.username = "William Vaurien"
        config.user.password = "xxxxxxxxxxxxxx"
        println(config)
        settings.save(config)
        val load = settings.load()
        println(load)
        
    }

    private fun loadRessource(path: String): String? {
        return Thread.currentThread().contextClassLoader.getResource(path).toExternalForm()
    }


}