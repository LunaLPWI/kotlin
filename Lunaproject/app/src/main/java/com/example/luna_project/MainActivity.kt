package com.example.luna_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luna_project.ui.theme.LunaprojectTheme
import profileScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunaprojectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("profile") { profileScreen(navController) }
                    composable("home") { MainScreen(navController) }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    profileScreen(navController)
}





