package com.example.luna_project.ui.theme.activities

import ProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.luna_project.components.LoginScreen
import com.example.luna_project.ui.theme.LunaprojectTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunaprojectTheme {
                ProfileScreen()
            }
        }
    }
}