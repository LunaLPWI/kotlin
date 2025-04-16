package com.example.luna_project.api

import com.example.luna_project.models.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/usuarios")
    suspend fun cadastrarUsuario(@Body usuario: Usuario): Response<Unit>
}