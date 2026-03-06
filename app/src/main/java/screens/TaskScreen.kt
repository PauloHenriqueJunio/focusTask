package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.TaskCard

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