package com.emorae.controllers

import com.emorae.module
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.*
import org.junit.Test

class MiniRedisControllerTest{

    @Test
    fun testRoot() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("bad command", response.content)
            }
        }
    }

    @Test
    fun testCommandSET() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/?cmd=SET mykey cool-value").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("OK", response.content)
            }
        }
    }

    @Test
    fun testCommandGET() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/?cmd=GET mykey").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("nil", response.content)
            }
        }
    }

    @Test
    fun testCommandDEL() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/?cmd=DEL mykey").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("OK", response.content)
            }
        }
    }

    @Test
    fun testCommandSetWithBadKey() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/?cmd=SET myk(ey value").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("bad key", response.content)
            }
        }
    }

    @Test
    fun testEmptyCommand() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/?cmd=").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("bad command", response.content)
            }
        }
    }

    @Test
    fun testGetRest() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/my-key2").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("nil", response.content)
            }
        }
    }

    @Test
    fun testDelRest() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Delete, "/my-key").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("OK", response.content)
            }
        }
    }

    @Test
    fun testPutRest() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Put, "/my-key").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("OK", response.content)
            }
        }
    }
}