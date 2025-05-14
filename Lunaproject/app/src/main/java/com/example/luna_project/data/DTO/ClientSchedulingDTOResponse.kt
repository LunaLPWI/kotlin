package com.example.luna_project.data.DTO

data class ClientSchedulingDTOResponse (
    val id: Long,
    val startDateTime: String,
    val items: List<EmployeeTaskDTO>,
    val nameEmployee: String,
    val status: String,
    val stablishmentName: String,
    val price: Double
)