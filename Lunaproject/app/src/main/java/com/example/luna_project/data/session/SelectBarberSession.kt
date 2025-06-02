package com.example.luna_project.data.session

import android.util.Log
import com.example.luna_project.data.models.AddressDTO
import com.example.luna_project.data.models.PlanDTO
import com.example.luna_project.data.models.Barbershop

object SelectBarberSession {
    var id: Long = -1L
    var name: String = ""
    var addressDTO: AddressDTO? = null  // Alterado para 'var' para permitir a modificação
    var planDTO: PlanDTO? = null  // Alterado para 'var' para permitir a modificação
    var cnpj: String = ""
    var openHour: String = ""
    var closeHour: String = ""
    var lat: Double = 0.0
    var logn: Double = 0.0
    var avarageRating: Double = 0.0

    // Função para limpar a sessão
    fun clearSession() {
        // Limpa todos os dados da barbearia selecionada
        id = -1L
        name = ""
        addressDTO = null
        planDTO = null
        cnpj = ""
        openHour = ""
        closeHour = ""
        lat = 0.0
        logn = 0.0
        avarageRating = 0.0
    }

    // Função para salvar a barbearia selecionada na sessão
    fun saveSelectedBarbershop(barbershop: Barbershop) {
        // Salva as informações da barbearia selecionada
        Log.d("BarbershopSelection", "Barbearia selecionada: ${barbershop.name}, Endereço: ${barbershop.id}")

        id = barbershop.id
        name = barbershop.name
        addressDTO = barbershop.addressDTO  // Supondo que o modelo Barbershop tenha um campo addressDTO
        planDTO = barbershop.planDTO  // Supondo que o modelo Barbershop tenha um campo planDTO
        cnpj = barbershop.cnpj
        openHour = barbershop.openHour
        closeHour = barbershop.closeHour
        lat = barbershop.lat
        logn = barbershop.logn
        avarageRating = barbershop.avarageRating

    }


}