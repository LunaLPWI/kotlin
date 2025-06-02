package com.example.luna_project.data.models

data class SchedulingRequest(
    val clientId: Long,
    val employeeId: Long,
    val startDateTime: String,
    val items: List<Long>
)