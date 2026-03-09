package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skynet.focustask.components.TaskCard
import com.skynet.focustask.viewmodel.FocusViewModel

@Composable
fun TaskScreen(viewModel: FocusViewModel, onTaskClick: (String) -> Unit) {
    var newTaskName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // A lista agora vem do Cérebro Global!
    val taskList = viewModel.taskList
    val isOrganizing = viewModel.isOrganizing

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Minhas Tarefas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = { viewModel.organizeTasksWithAI() },
                enabled = !isOrganizing && taskList.size > 1,
                modifier = Modifier.height(40.dp)
            ) {
                if (isOrganizing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("🧠 Organizar")
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            TextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("Label") },
                placeholder = { Text("Input") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                trailingIcon = {
                    if (newTaskName.isNotEmpty()) {
                        IconButton(onClick = { newTaskName = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpar texto")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                    .clickable {
                        if (newTaskName.isNotBlank()) {
                            showDialog = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Tarefa",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        if (showDialog) {
            AddTaskDialog(
                taskName = newTaskName,
                onDismiss = { showDialog = false },
                onConfirm = { name, deadline, difficulty, estimatedTime ->
                    viewModel.addTask(name, deadline, difficulty, estimatedTime)
                    newTaskName = ""
                    showDialog = false
                }
            )
        }

        LazyColumn {
            items(taskList) { task ->
                TaskCard(
                    task = task,
                    onClick = { onTaskClick(task.name) },
                    onToggleTimer = { viewModel.toggleTimer(task) } // Conectamos o botão ao motor do Cérebro!
                )
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    taskName: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var deadline by remember { mutableStateOf("Sem prazo") }
    var difficulty by remember { mutableStateOf("Média") }
    var estimatedTime by remember { mutableStateOf("1h") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar nova tarefa", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Nome: $taskName", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Prazo", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Hoje", "Amanhã", "3 dias", "1 semana").forEach { option ->
                            FilterChip(
                                selected = deadline == option,
                                onClick = { deadline = option },
                                label = { Text(option, fontSize = 12.sp) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("2 semanas", "1 mês", "Sem prazo").forEach { option ->
                            FilterChip(
                                selected = deadline == option,
                                onClick = { deadline = option },
                                label = { Text(option, fontSize = 12.sp) }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("Dificuldade", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Fácil", "Média", "Difícil").forEach { option ->
                        FilterChip(
                            selected = difficulty == option,
                            onClick = { difficulty = option },
                            label = { Text(option, fontSize = 12.sp) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("Tempo estimado", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("30min", "1h", "2h", "3h", "4h").forEach { option ->
                            FilterChip(
                                selected = estimatedTime == option,
                                onClick = { estimatedTime = option },
                                label = { Text(option, fontSize = 12.sp) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("1 dia", "2 dias", "3 dias", "1 semana", "2 semanas").forEach { option ->
                            FilterChip(
                                selected = estimatedTime == option,
                                onClick = { estimatedTime = option },
                                label = { Text(option, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(taskName, deadline, difficulty, estimatedTime) }) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}