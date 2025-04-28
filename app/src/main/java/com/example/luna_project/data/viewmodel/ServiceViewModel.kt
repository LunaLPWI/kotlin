package com.example.luna_project.data.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate

data class Barber(val name: String, val image: Int)
data class Service(val title: String, val price: String)

class ServiceViewModel : ViewModel() {
    private val _selectedTab: MutableState<Int> = mutableStateOf(0)
    val selectedTab: State<Int> get() = _selectedTab

    private val _selectedServices: MutableState<MutableList<Pair<String, String>>> = mutableStateOf(mutableListOf())
    val selectedServices: State<MutableList<Pair<String, String>>> get() = _selectedServices

    private val _selectedBarbers: MutableState<MutableList<Barber>> = mutableStateOf(mutableListOf())
    val selectedBarbers: State<MutableList<Barber>> get() = _selectedBarbers

    private val _selectedBarber: MutableState<Barber?> = mutableStateOf(null)
    val selectedBarber: State<Barber?> get() = _selectedBarber

    private val _selectedDate: MutableState<LocalDate?> = mutableStateOf(null)
    val selectedDate: State<LocalDate?> get() = _selectedDate

    private val _selectedHour: MutableState<String?> = mutableStateOf(null)
    val selectedHour: State<String?> get() = _selectedHour

    private val _selectedCategory: MutableState<String> = mutableStateOf("hair")
    val selectedCategory: State<String> get() = _selectedCategory

    fun setSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun addService(service: Pair<String, String>) {
        _selectedServices.value.add(service)
    }

    fun removeService(serviceTitle: String) {
        _selectedServices.value.removeIf { it.first == serviceTitle }
    }

    fun selectBarber(barber: Barber) {
        _selectedBarber.value = barber
    }

    fun deselectBarber() {
        _selectedBarber.value = null
    }

    fun addBarber(barber: Barber) {
        _selectedBarbers.value.add(barber)
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setSelectedHour(hour: String) {
        _selectedHour.value = hour
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun clearSelections() {
        _selectedTab.value = 0
        _selectedServices.value.clear()
        _selectedBarbers.value.clear()
        _selectedBarber.value = null
        _selectedDate.value = null
        _selectedHour.value = null
        _selectedCategory.value = "hair"
    }
}