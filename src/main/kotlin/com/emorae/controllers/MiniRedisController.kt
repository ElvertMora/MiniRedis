package com.emorae.controllers

import com.emorae.cache.MiniRedis
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

class MiniRedisController(
    route: Route,
    private val miniRedis: MiniRedis
) {
    init {
        route {
            get {
                val parameters = call.request.queryParameters
                val cmd = parameters["cmd"].toString()
                call.respondText(redis(cmd) ?: "")
            }
        }
    }

    private fun redis(cmd: String): String? {
        val array = cmd.split(" ")
        val validateKey = array.size > 1 && array[1].validateKey()
        return when (array[0]) {
            "SET" -> {
                if (validateKey) {
                    miniRedis.set(key = array[1], value = array[2], null, null)
                    "OK"
                } else {
                    "bad key"
                }
            }
            "GET" -> miniRedis.get(array[1])
            "DEL" -> {
                miniRedis.del(listOf(array[1]))
                "OK"
            }
            else -> "bad command"
        }

    }

}