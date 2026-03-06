package com.skynet.focustask
import ViewModel.FocusViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skynet.focustask.ui.theme.FocusTaskTheme
import screens.TaskScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skynet.focustask.screens.TaskDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Iniciamos o nosso Roteador aqui!
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // INJETANDO O CÉREBRO AQUI!
    val viewModel: FocusViewModel = viewModel()



    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            // Passamos o cérebro para a Tela 1
            TaskScreen(
                viewModel = viewModel,
                onTaskClick = { taskName -> navController.navigate("details/$taskName") }
            )
        }
        composable("details/{taskName}") { backStackEntry ->
            val taskName = backStackEntry.arguments?.getString("taskName") ?: "Tarefa"
            // Adicionamos o viewModel aqui também!
            TaskDetailScreen(
                taskName = taskName,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}