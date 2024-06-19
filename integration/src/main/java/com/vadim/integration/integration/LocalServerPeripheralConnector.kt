package com.vadim.integration.integration

import android.util.Log
import com.vadim.domain.integraion.PeripheralConnector
import com.vadim.domain.model.SafetyComfortState
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.response.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalServerPeripheralConnector @Inject constructor(): PeripheralConnector {
    init {
        startServer()
    }
    private lateinit var server: NettyApplicationEngine
    private fun startServer() {
        server = embeddedServer(Netty, port = 8080, module = Application::module, host = "0.0.0.0")
        server.start()
        Log.i("CSS","ServerStarted")
    }
    override fun onReceiveNewState(state: SafetyComfortState) {

    }
}

fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }

    routing {
        get("/") {
            call.respond(mapOf("message" to "Hello, world!"))
        }
    }
}