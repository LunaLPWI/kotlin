package com.example.luna_project.data.models


data class Barber(
    val id: Long,                // Em Kotlin, usamos 'val' ou 'var' ao invés de 'private' diretamente
    val name: String,
    val cpf: String,
    val email: String,
    val phoneNumber: String,
    val birthDay: String,     // Tipo LocalDate já está correto
    val establishmentName: String
)