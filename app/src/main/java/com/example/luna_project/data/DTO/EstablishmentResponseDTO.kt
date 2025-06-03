package com.example.luna_project.data.DTO

data class EstablishmentResponseDTO(
    val id: Long,
    val name: String,
    val addressDTO: AddressDTO,
    val cnpj: String,
    val openHour: String,
    val closeHour: String,
    val favorite: Boolean
)