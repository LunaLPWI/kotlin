package com.example.luna_project.data.models


data class User(
    val id: Long? = null, // id pode ser opcional no cadastro
    val name: String,
    val email: String,
    val cpf: String,
    val password: String,
    val phoneNumber: String,
    val birthDay: String, // usando LocalDate
    val roles: Set<String> = emptySet() // padrão USER se quiser
)