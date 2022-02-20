package com.emorae

import com.emorae.config.WebServerConfig
import com.emorae.services.ServiceComposer
import io.ktor.application.Application


fun main(args: Array<String>): Unit = io.ktor.server.tomcat.EngineMain.main(args)

fun Application.module() {
    val serviceComposer = ServiceComposer()
    WebServerConfig(application = this,
        serviceComposer = serviceComposer)
}

