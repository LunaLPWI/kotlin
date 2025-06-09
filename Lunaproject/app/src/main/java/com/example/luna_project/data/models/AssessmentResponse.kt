package com.example.luna_project.data.models

import com.google.gson.annotations.SerializedName

data class AssessmentResponse(
    @SerializedName("assessment_id")
    val assessmentId: Long,
    val establishmentName: String,
    val clientName: String,
    val rating: Double?,
    val messaging: String?
)