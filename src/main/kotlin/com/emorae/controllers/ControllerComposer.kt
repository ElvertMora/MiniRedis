package com.emorae.controllers

import com.emorae.services.ServiceComposer
import io.ktor.routing.Route

class ControllerComposer(
    route: Route,
    serviceComposer: ServiceComposer
) {
    val miniRedisController = MiniRedisController(route,serviceComposer.miniRedis)
    val miniRedisRESTController = MiniRedisRESTController(route,serviceComposer.miniRedis)
}