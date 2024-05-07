package com.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import models.Comanda
import java.util.Scanner

suspend fun main() {
    val client = HttpClient(CIO)
    val scan = Scanner(System.`in`)

    // Hacer una solicitud GET al servidor de comandas para obtener todas las comandas
    var response: HttpResponse = client.get("http://127.0.0.1:8080/comanda") {
        contentType(ContentType.Application.Json)
    }
    println("Status después del GET: ${response.status}")

    if (response.status.isSuccess()) {
        // Obtener todas las comandas del cuerpo de la respuesta
        val comandas = Json.decodeFromString<List<Comanda>>(response.bodyAsText())

        // Mostrar todas las comandas al usuario
        println("Comandas disponibles:")
        for (c in comandas) {
            println("ID de Comanda: ${c.idComanda} - Importe: ${c.importe}")
        }

        // Solicitar al usuario que ingrese el ID del comando que desea pagar
        println("Ingrese el ID del comando que desea pagar:")
        val comandaId = scan.nextInt()

        // Verificar si el ID ingresado es válido
        if (comandaId != null) {
            // Hacer una solicitud POST al servidor de comandas para pagar el comando seleccionado
            response = client.post("http://127.0.0.1:8080/comanda/$comandaId") {
                contentType(ContentType.Application.Json)
            }
            println("Status después del POST: ${response.status}")

            if (response.status.isSuccess()) {
                println("El comando con ID $comandaId ha sido pagado exitosamente.")
            } else {
                println("No se pudo pagar el comando con ID $comandaId. Código de estado: ${response.status}")
            }
        } else {
            println("ID de comando inválido.")
        }
    } else {
        println("No se pudieron obtener las comandas. Código de estado: ${response.status}")
    }

    client.close()
}