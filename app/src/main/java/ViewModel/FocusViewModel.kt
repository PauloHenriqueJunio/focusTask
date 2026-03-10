package com.skynet.focustask.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job // <-- IMPORT NOVO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.skynet.focustask.network.OllamaClient
import com.skynet.focustask.network.OllamaRequest

class Task(
    val name: String,
    var deadline: String = "Sem prazo",
    var difficulty: String = "Média",
    var estimatedTime: String = "1h",
    var priority: Int = 0
) {
    var timeElapsed by mutableIntStateOf(0)
    var isRunning by mutableStateOf(false)
    var timerJob: Job? = null
    var aiFeedback by mutableStateOf("Nenhum feedback ainda. Finalize a tarefa para gerar a análise.")
    var isLoadingFeedback by mutableStateOf(false)
}

class FocusViewModel : ViewModel() {
    val taskList = mutableStateListOf(
        Task("Estudar para apresentação do MVP", deadline = "amanhã", difficulty = "Alta", estimatedTime = "2h"),
        Task("Configurar API da IA", deadline = "hoje", difficulty = "Média", estimatedTime = "1h")
    )
    
    var isOrganizing by mutableStateOf(false)

    fun addTask(name: String, deadline: String = "Sem prazo", difficulty: String = "Média", estimatedTime: String = "1h") {
        taskList.add(Task(name = name, deadline = deadline, difficulty = difficulty, estimatedTime = estimatedTime))
    }

    fun getTaskByName(name: String): Task? {
        return taskList.find { it.name == name }
    }

    fun toggleTimer(task: Task) {
        task.isRunning = !task.isRunning

        if (task.isRunning) {
            task.timerJob?.cancel()
            task.timerJob = viewModelScope.launch {
                while (task.isRunning) {
                    delay(1000L)
                    task.timeElapsed++
                }
            }
        } else {
            task.timerJob?.cancel()
        }
    }

    fun getFeedbackFromAi(task: Task) {
        if (task.isRunning) {
            toggleTimer(task)
        }

        task.isLoadingFeedback = true
        task.aiFeedback = "Analisando o desempenho da IA..."

        viewModelScope.launch {
            try {
                val prompt = "Você é um assistente de produtividade. Informe quantos segundos o usuário gastou na tarefa,ou seja ele gastou: ${task.timeElapsed} segundos. Dê um feedback curto, profissional e encorajador de no máximo 5 frases sobre essa atividade."

                // Modelo para testes: altere aqui (ex: "gemma3:270m", "gemma3:1b")
                val request = OllamaRequest(model = "gemma3:1b", prompt = prompt)
                val result = OllamaClient.api.generateFeedback(request)

                task.aiFeedback = result.response
            } catch (e: Exception) {
                task.aiFeedback = "Erro ao conectar com a IA: ${e.message}. O Ollama está rodando no PC?"
            } finally {
                task.isLoadingFeedback = false
            }
        }
    }
    
    fun organizeTasksWithAI() {
        isOrganizing = true
        
        viewModelScope.launch {
            try {
                val prompt = buildOrganizationPrompt()
                // Modelo para testes: altere aqui (ex: "gemma3:270m", "gemma3:1b")
                val request = OllamaRequest(model = "gemma3:1b", prompt = prompt)
                val result = OllamaClient.api.generateFeedback(request)
                
                parseAndReorderTasks(result.response)
            } catch (e: Exception) {
                // Em caso de erro, mantém ordem atual
            } finally {
                isOrganizing = false
            }
        }
    }
    
    private fun buildOrganizationPrompt(): String {
        val tasksDescription = taskList.mapIndexed { index, task ->
            "${index + 1}. ${task.name} - prazo: ${task.deadline} - dificuldade: ${task.difficulty} - tempo: ${task.estimatedTime}"
        }.joinToString("\n")
        
        return """
Você é um assistente de produtividade especializado.

Organize as tarefas abaixo pela melhor ordem considerando:
- Prazo (tarefas urgentes primeiro)
- Dificuldade (equilibrar fáceis e difíceis)
- Tempo estimado (priorizar rápidas quando urgente)

Retorne APENAS os números na ordem ideal, separados por vírgula.
Exemplo: 3,1,2,4

Tarefas:
$tasksDescription

Resposta (apenas números separados por vírgula):
        """.trimIndent()
    }
    
    private fun parseAndReorderTasks(response: String) {
        try {
            // Extrai apenas números da resposta (ex: "3,1,2,4" ou "3, 1, 2, 4")
            val order = response.trim()
                .replace(Regex("[^0-9,]"), "")
                .split(",")
                .mapNotNull { it.trim().toIntOrNull() }
            
            if (order.size == taskList.size && order.toSet().size == order.size) {
                val reorderedTasks = order.mapNotNull { index ->
                    taskList.getOrNull(index - 1)
                }
                
                if (reorderedTasks.size == taskList.size) {
                    taskList.clear()
                    taskList.addAll(reorderedTasks)
                    
                    // Atualiza prioridades
                    taskList.forEachIndexed { index, task ->
                        task.priority = taskList.size - index
                    }
                }
            }
        } catch (e: Exception) {
            // Se falhar no parse, mantém ordem original
        }
    }
}