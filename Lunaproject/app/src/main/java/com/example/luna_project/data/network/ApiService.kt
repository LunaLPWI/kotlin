package com.example.luna_project.data.network


import com.example.luna_project.data.models.AssessmentRequest
import com.example.luna_project.data.models.AssessmentResponse
import com.example.luna_project.data.models.AssessmentUpdateDTO
import com.example.luna_project.data.models.Barber
import com.example.luna_project.data.models.CadastroResponse
import com.example.luna_project.data.models.ClientSchedulingDTOResponse
import com.example.luna_project.data.models.EstablishmentResponse
import com.example.luna_project.data.models.ResetPasswordModel
import com.example.luna_project.data.models.SchedulingRequest
import com.example.luna_project.data.models.SchedulingResponse
import com.example.luna_project.data.models.UserResponseLogin
import com.example.luna_project.data.models.User
import com.example.luna_project.data.models.UserLogin
import com.example.luna_project.presentation.components.barberSection.Task
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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
        @Query("lgn") lgn: Double
    ): Response<List<EstablishmentResponse>>
    @PATCH("clients/reset-password")
    fun resetPassword(@Body resetPasswordModel: ResetPasswordModel): Call<String>
    @GET("clients/establishment/{id}/employees")
    fun getEmployeesById(@Path("id") id: Long,@Header("Authorization") token: String): Call<List<Barber>>
    @GET("employee-tasks/{clientId}")
    fun getTasks(@Path("clientId") id: Long,@Header("Authorization") token: String): Call<List<Task>>
    @GET("schedules/vacant-schedules")
    fun getVacantSchedules(
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("employeeId") employeeId: Long,
        @Query("clientId") clientId: Long,
        @Header("Authorization") token: String
    ): Call<Set<String>>
    @POST("schedules")
    fun saveScheduling(
        @Body schedulingRequest: SchedulingRequest,
        @Header("Authorization") token: String
    ): Call<SchedulingResponse>
    @GET("establishments/search")
    suspend fun searchEstablishments(
        @Header("Authorization") token: String, // <-- Adicionado aqui para passar o token
        @Query("name") name: String
    ): Response<List<EstablishmentResponse>>

    @GET("schedules/client-schedules")
    suspend fun getSchedulingClient(
        @Header("Authorization") token: String, // <-- Adicionado aqui para passar o token
        @Query("start") start: String,
        @Query("clientId") clientId:Long
    ): Response<List<ClientSchedulingDTOResponse>>
    @DELETE("schedules/{id}")
    suspend fun deleteScheduling(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Void>

    @GET("schedules/lastscheduling/{clientId}")
    suspend fun getLastSchedulingClient(
        @Header("Authorization") token: String,
        @Path("clientId") clientId: Long
    ): Response<ClientSchedulingDTOResponse>

    @PUT("Assessment/{id}")
    suspend fun updateAssessment(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body dto: AssessmentUpdateDTO
    ): Response<AssessmentResponse>

    @GET("Assessment/client")
    suspend fun getAssessments(
        @Header("Authorization") token: String,
        @Query("clientId") clientId: Long?,
        @Query("timestamp") timestamp: String
    ): List<AssessmentResponse>

    @GET("Assessment")
    suspend fun getAssessmentsByEstablishment(
        @Header("Authorization") token: String,
        @Query("establishmentId") establishmentId: Long?,
    ): List<AssessmentResponse>








}
