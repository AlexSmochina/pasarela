package com.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import models.Comanda

suspend fun main() {
    val client = HttpClient(CIO)

    val response: HttpResponse = client.get("http://127.0.0.1:8080/comanda/{id?}") {
        url("http://127.0.0.1:8080/comanda/{id?}")
        contentType(ContentType.Application.Json)
    }
    println("Status després del GET: ${response.status}")

    val comanda = Json.decodeFromString<List<Comanda>>(response.bodyAsText())
    println(comanda)
    for (c in comanda) {
        println("IdComanda: ${c.idComanda} - Importe: ${c.importe}")
    }

    client.close()
}