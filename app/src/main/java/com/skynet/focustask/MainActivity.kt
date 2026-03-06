package com.skynet.focustask
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
import screens.TaskDetailScreen
import screens.TaskScreen


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

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        // TELA 1: Nossa lista de tarefas
        composable("home") {
            TaskScreen(
                // Quando clicar em uma tarefa, manda o roteador ir para a tela de detalhes
                onTaskClick = { taskName ->
                    navController.navigate("details/$taskName")
                }
            )
        }

        // TELA 2: Detalhes da tarefa selecionada (recebe o nome da tarefa pela "URL")
        composable("details/{taskName}") { backStackEntry ->
            val taskName = backStackEntry.arguments?.getString("taskName") ?: "Tarefa Desconhecida"
            TaskDetailScreen(
                taskName = taskName,
                // Quando clicar em voltar, desempilha a tela atual
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}