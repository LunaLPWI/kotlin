package com.example.luna_project.data.DTO

data class SchedulingResponseDTO(
    val id: Long,
    val startDateTime: String,
    val items: List<EmployeeTaskDTO>,
    val clientId: Long,
    val employeeId: Long,
    val statusScheduling: String
)