package com.example.luna_project.data.models

data class Barbershop(
    val id: Long,
    val name: String,
    val  addressDTO :AddressDTO,
    val  planDTO: PlanDTO?,
    val  cnpj: String,
    val  openHour: String,
    val  closeHour: String,
    val  lat:Double,
    val  logn:Double,
    val  avarageRating: Double

)