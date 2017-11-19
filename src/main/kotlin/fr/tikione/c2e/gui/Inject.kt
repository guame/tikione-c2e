package fr.tikione.c2e.gui

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import fr.tikione.c2e.core.model.config.CpcScrapperConfig
import fr.tikione.c2e.gui.service.GuiLauncherService
import fr.tikione.c2e.gui.service.GuiLauncherServiceImpl
import fr.tikione.c2e.gui.service.GuiSettingsService
import fr.tikione.c2e.gui.service.GuiSettingsServiceImpl

val kodein = Kodein {
    bind<GuiLauncherService>() with singleton { GuiLauncherServiceImpl() }
    bind<GuiSettingsService>() with singleton { GuiSettingsServiceImpl() }
    bind<CpcScrapperConfig>() with singleton { 
        val settings : GuiSettingsService = instance()
        settings.load() 
    }
}
