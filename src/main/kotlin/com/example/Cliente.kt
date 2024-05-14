package com.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Comanda
import java.util.Scanner

val scan = Scanner(System.`in`)

suspend fun main() {

    var continuar = true
    while (continuar) {
        println("BIENVENID@ AL PORTAL DE PAGO!")
        println("1- identificarse \n2- Salir")
        print("opcion: ")
        val option = scan.nextInt()
        scan.nextLine()
        when(option){
            1 -> { iniciarSecion() }
            2 -> { continuar = false }
        }
    }
}

suspend fun iniciarSecion() {
    val urlServer = "http://127.0.0.1:8081/comanda"

    println()
    print("nombre: ")
    val nom  = scan.nextLine()
    var continuar = true
    while (continuar){
        println("BIENVENID@ AL PORTAL DE PAGO!")
        println("1- Visualizar Comandas \n2- Pagar Comanda \n3- Salir")
        print("opcion: ")
        val option = scan.nextInt()

        when(option){
            1 -> { visualizarComandas( urlServer, nom ) }
            2 -> { pagarComandas( urlServer ) }
            3 -> { continuar = false }
        }
    }
}

suspend fun pagarComandas(urlServer: String) {

    println("Introduce el id de la comanda que quieras pagar.")
    print("Id: ")
    val id = scan.nextInt()

    val client = HttpClient(CIO){
        install(HttpTimeout)
    }
    val urlAlta = "/pagar/$id"
    val urlFinal = urlServer + urlAlta

    val response: HttpResponse = client.get(urlFinal) {
        timeout {
            requestTimeoutMillis = 300*1000
        }
    }

    val texto = response.bodyAsText()
    println(texto)
    if ( texto == "Pagado Correctamente!" ){
        println("¡OPERACION EXITOSA!")
    } else {
        println("¡No ha sido posible!")
    }

    client.close()
}

suspend fun visualizarComandas(urlServer: String, nom: String) {
    val client = HttpClient(CIO){
        install(HttpTimeout)
    }

    val urlAlta = "/$nom"
    val urlFinal = urlServer + urlAlta

    val response: HttpResponse = client.get(urlFinal) {
        timeout {
            requestTimeoutMillis = 300*1000
        }
    }

    println("\nLista de Comandas:")
    val cmd = response.bodyAsText()
    val comandas = Json.decodeFromString< List<Comanda> >( cmd )
    comandas.forEach{
        println( "idComanda - ${it.idComanda}, importe - ${it.importe}, pagado - ${it.pagado}  " )
    }

    client.close()
}