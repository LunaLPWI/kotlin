package com.example.luna_project.data.models

data class EmployeeTask(
    val id: Long,
    val name: String,
    val description: String,
    val value: Double?,
    val duration: Int?
)