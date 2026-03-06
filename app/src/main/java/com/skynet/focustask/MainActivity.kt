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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack


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
                    text = "Placeholder", // Como no seu Figma (depois podemos mudar para algo real)
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier
                        .padding(end = 14.dp),
                    text = taskName, // Aqui fica o nome real da tarefa que você digitou
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 3. O espaço do cronômetro vazio ("Tt") da direita do seu Figma
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    progress = { 0.50f },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .size(35.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 3.7.dp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .padding(top = 3.dp),
                    text = "00:00",
                    fontSize = 15.sp,
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. TOPO: Barra de progresso e Botão Voltar igual ao Figma
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
        ) {

            // O Botão de "<- Voltar" personalizado do seu Figma
            Surface(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp),
                onClick = onBackClick,
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Voltar",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // 2. O CARD DA TAREFA (Limpo, sem o cronômetro circular)
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = taskName.take(1).uppercase(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Em andamento", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = taskName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 3. ÁREA DE TEXTO (Onde o Lorem Ipsum estava no Figma)

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                        text = "Análise da Inteligência Artificial",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Feedback e métricas", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Aqui entrará o feedback inteligente da IA analisando o tempo que você gastou nesta atividade.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }

        }




        // Este Spacer com weight(1f) é um truque ninja! Ele empurra tudo que vem abaixo dele para o final da tela.
        Spacer(modifier = Modifier.weight(1f))

        LinearProgressIndicator(
            progress = { 0.75f }, // Fixo em 50% só para o visual
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 40.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        // 4. BOTÕES DE AÇÃO (Secondary e Primary do Figma)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End // Joga os botões para a direita
        ) {
            OutlinedButton(onClick = { /* Zero lógica */ }) {
                Text("Pausar") // O botão Secondary (borda vazada)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Zero lógica */ }) {
                Text("Finalizar") // O botão Primary (preenchido)
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Espaço extra no fundo
    }
}