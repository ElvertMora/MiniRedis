package com.emorae.config

import com.emorae.controllers.ControllerComposer
import com.emorae.services.ServiceComposer
import io.ktor.application.Application
import io.ktor.routing.route
import io.ktor.routing.routing

class WebServerConfig(
    application: Application,
    serviceComposer: ServiceComposer
) {
    init {
        application.apply {
            routing {
                route("/") {
                    ControllerComposer(route = this, serviceComposer = serviceComposer)
                }
            }
        }
    }
}