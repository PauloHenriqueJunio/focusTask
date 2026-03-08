package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skynet.focustask.viewmodel.FocusViewModel

@Composable
fun TaskDetailScreen(taskName: String, viewModel: FocusViewModel, onBackClick: () -> Unit) {
    // 1. Busca a tarefa viva no cérebro
    val task = viewModel.getTaskByName(taskName)

    // Segurança: Se a tarefa não existir, sai da tela (evita crash do app)
    if (task == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    // 2. Matemática da Barra de Progresso (Mesma do Card)
    val totalTime = 60
    val progress = if (totalTime > 0) task.timeElapsed.toFloat() / totalTime else 0f

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // TOPO: Botão Voltar
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp)
        ) {
            Surface(
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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Voltar", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        // CARD DA TAREFA
        OutlinedCard(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    // ANTES: onClick = { task.isRunning = !task.isRunning }
                    onClick = { viewModel.toggleTimer(task) } // AGORA: Chama o motor do cérebro!
                ) {
                    Text(if (task.isRunning) "Pausar" else "Retomar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* Lógica de Finalizar futura */ }) {
                    Text("Finalizar")
                }
            }
        }

        // ÁREA DA IA
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(text = "Análise da Inteligência Artificial", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Feedback e métricas", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Lorem ipsum dolor sit amet... Aqui entrará o feedback inteligente da IA analisando o tempo que você gastou nesta atividade.",
                    fontSize = 16.sp, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BARRA DE PROGRESSO ANIMADA (Lê o progress vivo da tarefa!)
        LinearProgressIndicator(
            progress = { progress }, // Fixo em 50% só para o visual
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 40.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        // BOTÕES DE AÇÃO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                // O botão Secundário agora altera o ViewModel!
                onClick = { viewModel.toggleTimer(task) }
            ) {
                // Se estiver rodando exibe "Pausar", senão "Retomar"
                Text(if (task.isRunning) "Pausar" else "Retomar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Lógica de Finalizar futura */ }) {
                Text("Finalizar")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}