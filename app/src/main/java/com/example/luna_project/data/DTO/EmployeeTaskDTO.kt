package com.example.luna_project.data.DTO

data class EmployeeTaskDTO(
    val id: Long,
    val name: String,
    val description: String,
    val value: Double?,
    val duration: Int?
)