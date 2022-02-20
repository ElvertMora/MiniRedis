package com.emorae.controllers

import com.emorae.cache.MiniRedis
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

class MiniRedisRESTController(
    route: Route,
    private val miniRedis: MiniRedis
) {
    init {
        route.route("/{key}") {
            get {
                val key = call.parameters["key"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                call.respondText(miniRedis.get(key))
            }
            put {
                val key = call.parameters["key"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val value: String = call.receive(String::class)
                call.respondText(miniRedis.set(key,value,null,null))
            }
            delete {
                val key = call.parameters["key"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                miniRedis.del(listOf(key))
                call.respondText("OK")
            }
        }
    }
}