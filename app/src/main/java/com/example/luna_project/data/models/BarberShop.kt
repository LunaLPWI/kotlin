package com.example.luna_project.data.models

import com.example.luna_project.data.DTO.AddressDTO
import com.example.luna_project.data.DTO.PlanDTO
import java.sql.Time

data class Barbershop(
    val id: Long,
    val name: String,
    val  addressDTO :AddressDTO,
    val  planDTO: PlanDTO?,
    val  cnpj: String,
    val  openHour: String,
    val  closeHour: String,
    val  lat:Double,
    val  logn:Double

)