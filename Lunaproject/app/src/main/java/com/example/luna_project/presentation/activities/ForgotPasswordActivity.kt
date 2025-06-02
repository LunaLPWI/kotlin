package com.example.luna_project.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.luna_project.presentation.components.ForgotPasswordScreen
import com.example.luna_project.ui.theme.LunaprojectTheme

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunaprojectTheme {
                ForgotPasswordScreen()
            }
        }
    }
}