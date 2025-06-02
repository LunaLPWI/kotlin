package com.example.luna_project.data.models

data class ClientSchedulingDTOResponse (
    val id: Long,
    val startDateTime: String,
    val items: List<EmployeeTask>,
    val nameEmployee: String,
    val status: String,
    val stablishmentName: String,
    val price: Double
)