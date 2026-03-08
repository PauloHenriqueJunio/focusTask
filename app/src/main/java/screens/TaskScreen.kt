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

    // A lista agora vem do Cérebro Global!
    val taskList = viewModel.taskList

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Minhas Tarefas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
                            viewModel.addTask(newTaskName) // Salva no cérebro!
                            newTaskName = ""
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