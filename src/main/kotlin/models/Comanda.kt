package models

import kotlinx.serialization.Serializable

@Serializable
data class Comanda(
    val idComanda: Int,
    val importe: Float
)

val comandaStorage = mutableListOf<Comanda>()