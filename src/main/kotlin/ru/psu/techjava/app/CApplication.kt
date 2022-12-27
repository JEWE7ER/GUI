package ru.psu.techjava.app

import javafx.stage.Stage
import ru.psu.techjava.view.CViewMain
import tornadofx.*
import java.util.*
import java.net.InetAddress

class CApplication: App(CViewMain::class, Styles::class){
    private val api: Rest by inject()
    init {
        //Язык интерфейса приложения.
        FX.locale = Locale("ru")
        //Базовая часть адреса для подключения к API сервера.
        val ipAddress = "localhost"//getSystemIP()
        api.baseURI = "http://${ipAddress}:8080/"
    }
    //автоматичекое получение ip
    private fun getSystemIP(): String? {
        return try {
            InetAddress.getLocalHost().hostAddress
        } catch (e: Exception){
            println("Проверьте подключение к сети")
            null
        }
    }

    override fun start(stage: Stage) {
        with(stage) {
            minWidth = 600.0
            minHeight = 400.0
            super.start(this)
        }
    }
}