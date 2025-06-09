package com.example.luna_project.presentation.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luna_project.data.models.AssessmentResponse
import com.example.luna_project.data.models.AssessmentUpdateDTO
import com.example.luna_project.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AssessmentViewModel() : ViewModel() {
    private val _updateResult = MutableStateFlow<Boolean?>(null)
    val updateResult: StateFlow<Boolean?> = _updateResult

    private val _assessments = MutableStateFlow<List<AssessmentResponse>>(emptyList())
    val assessments: StateFlow<List<AssessmentResponse>> = _assessments

    fun fetchAssessments(token: String, userId: Long, timestamp: LocalDateTime, context: Context) {
        viewModelScope.launch {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val formattedTimestamp = timestamp.format(formatter)

                val result = RetrofitClient.apiService.getAssessments("Bearer $token", userId, formattedTimestamp)
                _assessments.value = result
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao buscar avaliações", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateAssessment(id: Long, rating: Double?, messaging: String?, token: String) {
        viewModelScope.launch {
            try {
                val dto = AssessmentUpdateDTO(rating, messaging)
                val response = RetrofitClient.apiService.updateAssessment("Bearer $token",id, dto)
                _updateResult.value = response.isSuccessful
            } catch (e: Exception) {
                _updateResult.value = false
            }
        }
    }

    private val _assessmentsByEstablishment = MutableStateFlow<List<AssessmentResponse>>(emptyList())
    val assessmentsByEstablishment: StateFlow<List<AssessmentResponse>> = _assessmentsByEstablishment

    fun fetchAssessmentsByEstablishment(token: String, establishmentId: Long?) {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.apiService.getAssessmentsByEstablishment("Bearer $token", establishmentId)
                _assessmentsByEstablishment.value = result
            } catch (e: Exception) {
                _assessmentsByEstablishment.value = emptyList()
            }
        }
    }
}
