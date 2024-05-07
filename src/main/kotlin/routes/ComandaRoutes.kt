package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Comanda
import models.comandaStorage

fun Route.comandaRouting() {
    route("/comanda") {
        get {
            if (comandaStorage.isNotEmpty()) {
                call.respond(comandaStorage)
            } else {
                call.respondText("No se han encontrado la comanda", status = HttpStatusCode.OK)
            }
        }
        post {
            val mensaje = call.receive<Comanda>()
            comandaStorage.add(mensaje)
            call.respondText("El mensaje se ha guardado corectamente", status = HttpStatusCode.Created)
        }
    }
}