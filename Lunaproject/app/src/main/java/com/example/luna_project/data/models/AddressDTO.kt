package com.example.luna_project.data.models

import java.io.Serializable

data class AddressDTO(
    var cep: String? = null,
    var logradouro: String? = null,
    var number: Int? = null,
    var complemento: String? = null,
    var cidade: String? = null,
    var bairro: String? = null,
    var uf: String? = null
): Serializable
{
    fun formatAddress(): String {
        val addressBuilder = StringBuilder()

        logradouro?.takeIf { it.isNotBlank() }?.let {
            addressBuilder.append(it)
        }

        number?.let {
            addressBuilder.append(", ").append(it)
        }

        bairro?.takeIf { it.isNotBlank() }?.let {
            addressBuilder.append(" - ").append(it)
        }

        cidade?.takeIf { it.isNotBlank() }?.let {
            addressBuilder.append(", ").append(it)
        }

        uf?.takeIf { it.isNotBlank() }?.let {
            addressBuilder.append(" - ").append(it)
        }

        cep?.takeIf { it.isNotBlank() }?.let {
            addressBuilder.append(", CEP: ").append(it)
        }

        return addressBuilder.toString().trim()
    }
}

