package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.comandaRouting

fun Application.configureRouting() {
    routing {
        comandaRouting()
    }
}
