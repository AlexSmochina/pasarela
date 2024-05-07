package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Comanda
import models.comandaStorage

fun Route.comandaRouting() {
    route("/comanda") {
        get ("{idComanda?}") {
            val id = call.parameters["idComanda"]?.toIntOrNull()
            val comandas: List<Comanda> = comandaStorage.filter {
                it.idComanda == id
            }
            if (comandas.isEmpty()){
                return@get call.respondText (
                    "No comanda with id: $id",
                    status = HttpStatusCode.NotFound
                )
            }

            call.respond(comandas)
        }
    }
}