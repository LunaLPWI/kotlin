package com.example.luna_project.data.models

data class SchedulingResponse(
    val id: Long,
    val startDateTime: String,
    val items: List<EmployeeTask>,
    val clientId: Long,
    val employeeId: Long,
    val statusScheduling: String
)