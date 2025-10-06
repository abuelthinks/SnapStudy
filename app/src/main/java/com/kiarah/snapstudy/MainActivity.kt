package com.kiarah.snapstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiarah.snapstudy.ui.theme.SnapStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnapStudyTheme {
                SnapStudyApp()
            }
        }
    }
}

@Composable
fun SnapStudyApp() {
    val navController = rememberNavController()
    val viewModel: SnapStudyViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onTakePhoto = { navController.navigate("camera") }
            )
        }
        composable("camera") {
            CameraScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPhotoTaken = { navController.navigate("result") }
            )
        }
        composable("result") {
            ResultScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack("home", false) },
                onTakeAnother = {
                    navController.popBackStack("home", false)
                    navController.navigate("camera")
                }
            )
        }
    }
}
