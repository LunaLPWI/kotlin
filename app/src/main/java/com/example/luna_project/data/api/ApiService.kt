package com.example.luna_project.data.api

import com.example.luna_project.data.DTO.CadastroResponse
import com.example.luna_project.data.DTO.EstablishmentResponse
import com.example.luna_project.data.DTO.ResetPasswordDTO
import com.example.luna_project.data.DTO.UserResponseLogin
import com.example.luna_project.data.models.User
import com.example.luna_project.data.models.UserLogin
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("clients") // <-- Ajusta para o endpoint certo
    suspend fun cadastrarUsuario(@Body user: User): Response<CadastroResponse>
    @POST("clients/login")
    suspend fun loginUsuario(@Body userLogin: UserLogin): Response<UserResponseLogin>
    @GET("establishments/nearbyestablishments")
    suspend fun getBarbershops(
        @Query("lat") lat: Double,
        @Query("logn") logn: Double
    ): Response<List<EstablishmentResponse>>
    @POST("establishments/nearbyestablishments")
    suspend fun getBarbershops(
        @Header("Authorization") token: String, // <-- Adicionado aqui para passar o token
        @Query("lat") lat: Double,
        @Query("logn") logn: Double
    ): Response<List<EstablishmentResponse>>
    @PATCH("clients/reset-password")
    fun resetPassword(@Body resetPasswordDTO: ResetPasswordDTO): Call<String>

}
