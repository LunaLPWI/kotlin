package com.example.luna_project.data.models

data class UserResponseLogin (
    val id: Long,
    val name: String,
    val email: String,
    val cpf: String,
    val token: String,
    val phoneNumber: String,
    val birthDay: String,
    val roles: List<String>
)

