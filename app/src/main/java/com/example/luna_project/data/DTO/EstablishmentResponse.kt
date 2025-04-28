package com.example.luna_project.data.DTO

import android.R
import java.sql.Time

data class EstablishmentResponse (
    val id: Long,
    val name: String,
    val addressDTO: AddressDTO,
    val  planDTO: PlanDTO,
    val  cnpj: String,
    val  openHour: String,
    val  closeHour: String,
)