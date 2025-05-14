package com.example.luna_project.data.session

import com.example.luna_project.data.models.Barbershop

object UserSession {
    var id: Long = -1L
    var name: String = ""
    var email: String = ""
    var cpf: String = ""
    var token: String = ""
    var phoneNumber: String = ""
    var birthDay: String = ""
    var roles: List<String> = emptyList()

    fun clearSession() {
        id = -1L
        name = ""
        email = ""
        cpf = ""
        token = ""
        phoneNumber = ""
        birthDay = ""
        roles = emptyList()
    }
}
