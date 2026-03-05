package com.skynet.focustask
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skynet.focustask.ui.theme.FocusTaskTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear


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

// ---------------------------------------------------
// ROTEADOR (Gerenciador de Telas)
// ---------------------------------------------------
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

// ---------------------------------------------------
// TELA 1 (Home)
// ---------------------------------------------------
@Composable
fun TaskScreen(onTaskClick: (String) -> Unit) {
    var newTaskName by remember { mutableStateOf("") }
    val taskList = remember { mutableStateListOf("Estudar para apresentação do MVP", "Configurar API da IA") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Mantemos o título, mas você pode tirar se preferir seguir 100% o Figma
        Text(
            text = "Minhas Tarefas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // O TOPO DA TELA (Input + Botão +)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // 1. O Campo de Texto estilo Figma (Preenchido e sem linha embaixo)
            TextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("Label") }, // Igual ao seu desenho
                placeholder = { Text("Input") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp), // Bordas arredondadas
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent, // Tira a linha de baixo
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                trailingIcon = {
                    // O ícone de "X" só aparece se tiver algum texto digitado
                    if (newTaskName.isNotEmpty()) {
                        IconButton(onClick = { newTaskName = "" }) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Clear,
                                contentDescription = "Limpar texto"
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 2. O Botão de "+" (Quadrado arredondado com destaque)
            Box(
                modifier = Modifier
                    .size(56.dp) // Um quadrado perfeito alinhado com a altura do input
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer, // O roxinho do seu Figma
                        shape = RoundedCornerShape(16.dp) // Borda bem arredondada
                    )
                    .clickable {
                        if (newTaskName.isNotBlank()) {
                            taskList.add(newTaskName)
                            newTaskName = "" // Limpa o campo após adicionar
                        }
                    },
                contentAlignment = Alignment.Center // Centraliza o + no meio do quadrado
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "Adicionar Tarefa",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer // Cor do ícone combinando com o fundo
                )
            }
        }

        // A Lista de Tarefas (não mexemos aqui)
        LazyColumn {
            items(taskList) { task ->
                TaskCard(taskName = task, onClick = { onTaskClick(task) })
            }
        }
    }
}

@Composable
fun TaskCard(taskName: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() }, // Mantém o clique para navegar
        colors = CardDefaults.outlinedCardColors(
            // Usa uma cor de superfície clarinha (padrão M3) em vez de cinza escuro
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), // Borda bem arredondada igual ao seu Figma
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Sem sombra, estilo "flat" moderno
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Bolinha com a inicial da tarefa (Igual a letra "A" roxinha do seu Figma)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = taskName.take(1).uppercase(), // Pega a primeira letra do que você digitou
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Textos no meio (Header e Subhead)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Header", // Como no seu Figma (depois podemos mudar para algo real)
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = taskName, // Aqui fica o nome real da tarefa que você digitou
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 3. O espaço do cronômetro vazio ("Tt") da direita do seu Figma
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tt",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ---------------------------------------------------
// TELA 2 (Detalhes da Tarefa)
// ---------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(taskName: String, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Uma TopAppBar clássica do Android com botão de voltar
        TopAppBar(
            title = { Text("Detalhes da Tarefa") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = taskName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Aqui ficará a interface que você desenhou no Figma para a Tela 2, com o círculo de progresso, botões de ação e o feedback da Inteligência Artificial.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}