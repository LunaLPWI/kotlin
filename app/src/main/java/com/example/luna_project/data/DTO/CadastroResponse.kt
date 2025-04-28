package com.example.luna_project.data.DTO

data class CadastroResponse(
    val id: Int,
    val name: String,
    val email: String,
    val cpf: String,
    val phoneNumber: String,
    val birthDay: String,
    val roles: List<String>
)