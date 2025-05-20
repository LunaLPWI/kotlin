package com.example.luna_project.ui.theme.activities

import android.os.Bundle
import com.example.luna_project.data.models.SchedulingViewModel
import com.example.luna_project.ui.theme.components.SucessefullScreenScheduling
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.luna_project.ui.theme.LunaprojectTheme


class SucessefullScreenActivity : ComponentActivity(){
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                LunaprojectTheme {
                    SucessefullScreenScheduling()
                }
            }
        }
}