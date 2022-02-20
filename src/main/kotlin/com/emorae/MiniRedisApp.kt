package com.emorae

import com.emorae.cache.data.Register
import com.emorae.config.WebServerConfig
import com.emorae.services.ServiceComposer
import io.ktor.application.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
val cacheContext = newSingleThreadContext("CounterContext")
private val cache: MutableMap<String, Register> = mutableMapOf()
val mutex = Mutex()


@ExperimentalCoroutinesApi
fun main(args: Array<String>): Unit = runBlocking {
    // confine everything to a single-threaded context
    withContext(cacheContext) { io.ktor.server.tomcat.EngineMain.main(args) }
}

fun Application.module() {
    val serviceComposer = ServiceComposer(cache,mutex)
    WebServerConfig(
        application = this,
        serviceComposer = serviceComposer
    )
}

