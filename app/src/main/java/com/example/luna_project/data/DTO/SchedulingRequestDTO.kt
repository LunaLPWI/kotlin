package com.example.luna_project.data.DTO

data class SchedulingRequestDTO(
    val clientId: Long,
    val employeeId: Long,
    val startDateTime: String,
    val items: List<Long>
)