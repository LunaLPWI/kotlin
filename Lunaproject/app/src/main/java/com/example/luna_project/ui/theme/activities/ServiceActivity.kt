package com.example.luna_project.ui.theme.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.luna_project.components.ServiceScreen
import com.example.luna_project.ui.theme.LunaprojectTheme

class ServiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunaprojectTheme {
                ServiceScreen()
            }
        }
    }
}