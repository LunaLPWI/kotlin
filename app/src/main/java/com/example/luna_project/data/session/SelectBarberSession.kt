package com.example.luna_project.data.session

import com.example.luna_project.data.models.Barbershop
object SelectBarberSession {
    var id: Long = -1L
    var name: String = ""
    var email: String = ""
    var cpf: String = ""
    var token: String = ""
    var phoneNumber: String = ""
    var birthDay: String = ""
    var roles: List<String> = emptyList()
    var selectedBarbershop: Barbershop? = null  // Agora, temos a referência à barbearia selecionada

    // Função para limpar a sessão
    fun clearSession() {
        id = -1L
        name = ""
        email = ""
        cpf = ""
        token = ""
        phoneNumber = ""
        birthDay = ""
        roles = emptyList()
        selectedBarbershop = null  // Limpa a barbearia selecionada
    }

    // Função para salvar a barbearia selecionada na sessão
    fun saveSelectedBarbershop(barbershop: Barbershop) {
        selectedBarbershop = barbershop
    }
}