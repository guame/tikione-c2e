package fr.tikione.c2e

import android.content.res.AssetManager
import com.github.salomonbrys.kodein.instance
import compat.Tools
import fr.tikione.c2e.service.html.HtmlWriterService
import fr.tikione.c2e.service.index.IndexWriterService
import fr.tikione.c2e.service.web.CPCAuthService
import fr.tikione.c2e.service.web.scrap.CPCReaderService
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

object Main {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    var DEBUG = false
    lateinit var VERSION: String
    private val VERSION_URL = "https://raw.githubusercontent.com/jonathanlermitage/tikione-c2e/master/uc/latest_version.txt"
    private val PAUSE_BETWEEN_MAG_DL = 30L

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            VERSION = Tools.resourceAsStr("version.txt", AssetManager())    
        }
        catch (e: Exception) {
            VERSION = "unknown"
        }
        
        log.info("TikiOne C2E version {}, Java {}, {} {} by {}, on {} with {} file encoding", VERSION,
                System.getProperty("java.version"),
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.version"),
                System.getProperty("java.vm.vendor"),
                System.getProperty("os.name"),
                System.getProperty("file.encoding"))
        try {
            log.debug("retrieve last released version at: {} ", VERSION_URL)
            val latestVersion = Jsoup.connect(VERSION_URL).get().text().trim()
            if (VERSION != latestVersion) {
                log.warn("<< une nouvelle version de TikiOne C2E est disponible (" + latestVersion + "), " +
                        "rendez-vous sur https://github.com/jonathanlermitage/tikione-c2e/releases >>")
            }
        } catch (e: Exception) {
            log.warn("impossible de verifier la presence d'une nouvelle version de TikiOne C2E", e)
        }
        val argsToShow = args.clone()
        if (argsToShow.isNotEmpty()) {
            argsToShow[0] = "(identifiant)"
            argsToShow[1] = "(mot de passe)"
        }
        log.info("les parametres de lancement sont : {}", Arrays.toString(argsToShow))
        val argsList = Arrays.asList(*args)
        DEBUG = argsList.contains("-debug")
        startCLI(*args)
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun startCLI(vararg args: String) {
        assert(args.size > 2)
        val switchList = Arrays.asList(*args).subList(2, args.size)
        val doList = switchList.contains("-list")
        val doIncludePictures = !switchList.contains("-nopic")
        val doIndex = switchList.contains("-index")
        var doHtml = false
        val doAllMags = switchList.contains("-cpcall")
        val doResize = args.firstOrNull { it.startsWith("-resize") }?.substring("-resize".length)
        val cpcAuthService: CPCAuthService = kodein.instance()
        val cpcReaderService: CPCReaderService = kodein.instance()

        val auth = cpcAuthService.authenticate(args[0], args[1])
        val headers = cpcReaderService.listDownloadableMagazines(auth)
        val magNumbers = ArrayList<String>()
        if (doAllMags) {
            magNumbers.addAll(headers)
            doHtml = true
        } else {
            args.filter { it.startsWith("-cpc") }.forEach {
                magNumbers.add(it.substring("-cpc".length))
                doHtml = true
            }
        }
        if (doList) {
            log.info("les numeros disponibles sont : {}", headers)
        }

        if (doHtml) {
            if (magNumbers.size > 1) {
                log.info("telechargement des numeros : {}", magNumbers)
            }
            for (i in magNumbers.indices) {
                val magNumber = magNumbers[i]
                val magazine = cpcReaderService.downloadMagazine(auth, magNumber)
                val file = File("CPC" + magNumber
                        + (if (doIncludePictures) "" else "-nopic")
                        + (if (doResize == null) "" else "-resize$doResize")
                        + ".html")
                val writerService: HtmlWriterService = kodein.instance()
                writerService.write(magazine, file, doIncludePictures, doResize)
                if (i != magNumbers.size - 1) {
                    log.info("pause de ${PAUSE_BETWEEN_MAG_DL}s avant de telecharger le prochain numero")
                    TimeUnit.SECONDS.sleep(PAUSE_BETWEEN_MAG_DL)
                    log.info(" ok\n")
                }
            }
        }

        if (doIndex) {
            log.info("creation de l'index de tous les numeros disponibles")
            val file = File("CPC-index.csv")
            val writerService: IndexWriterService = kodein.instance()
            writerService.write(auth, headers, file)
        }

        log.info("termine !")
    }
}
