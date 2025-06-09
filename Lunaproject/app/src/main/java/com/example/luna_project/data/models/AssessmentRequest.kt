package com.example.luna_project.data.models

data class AssessmentRequest(
    val establishmentId: Long,
    val schedulingId: Long,
    val rating: Double,
    val messaging: String
)