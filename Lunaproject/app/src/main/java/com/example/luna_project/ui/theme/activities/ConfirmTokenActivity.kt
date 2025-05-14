package com.example.luna_project.ui.theme.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.luna_project.components.RegisterScreen
import com.example.luna_project.ui.theme.LunaprojectTheme

class ConfirmTokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunaprojectTheme {
                RegisterScreen()
            }
        }

    }
}