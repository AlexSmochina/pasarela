package routes

import com.example.pagarComandas
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import models.Comanda

fun Route.comandaRouting() {
    route("/comanda") {
        get ("/{idComanda?}") {
            val nom = call.parameters["idComanda"]?:return@get call.respondText (
                "Missing NOM",
                status = HttpStatusCode.NotFound
            )

            val comandas: List<Comanda> = servidorEcommerce(nom)
            if (comandas.isEmpty()){
                return@get call.respondText (
                    "No comanda with id: $nom",
                    status = HttpStatusCode.NotFound
                )
            }

            call.respond(comandas)
        }
        get("/pagar/{id?}") {
            val id = call.parameters["id"]?:return@get call.respondText (
                "Missing Id",
                status = HttpStatusCode.NotFound
            )

            val texto = pagos(id)
            println(texto)
            if (texto == "Resivido Correctamente!"){
                call.respondText("Pagado Correctamente!", status = HttpStatusCode.Created)
            } else {
                call.respondText("No pagado Correctamente!", status = HttpStatusCode.Created)
            }

        }
    }
}

suspend fun servidorEcommerce(name: String): List<Comanda>{
    val urlServer = "http://127.0.0.1:8080"
    val urlAlta = "/client/order/$name"
    val urlFinal = urlServer + urlAlta

    val client = HttpClient(CIO){
        install(HttpTimeout)
    }

    val response: HttpResponse = client.get(urlFinal) {
        timeout {
            requestTimeoutMillis = 300*1000
        }
    }

    println("\nLista de Comandas:")
    val cmd = response.bodyAsText()
    val comandas = Json.decodeFromString< List<Comanda> >( cmd )

    client.close()

    return comandas
}

suspend fun pagos(id: String): String {
    val urlServer = "http://127.0.0.1:8080"
    val urlAlta = "/comanda/pagos/$id"
    val urlFinal = urlServer + urlAlta

    val client = HttpClient(CIO){
        install(HttpTimeout)
    }

    val response: HttpResponse = client.get(urlFinal) {
        timeout {
            requestTimeoutMillis = 300*1000
        }
    }

    client.close()

    return response.bodyAsText()
}