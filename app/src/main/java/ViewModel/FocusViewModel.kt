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

class Task(val name: String) {
    var timeElapsed by mutableIntStateOf(0)
    var isRunning by mutableStateOf(false)
    var timerJob: Job? = null
    var aiFeedback by mutableStateOf("Nenhum feedback ainda. Finalize a tarefa para gerar a análise.")
    var isLoadingFeedback by mutableStateOf(false)
}

class FocusViewModel : ViewModel() {
    val taskList = mutableStateListOf(
        Task("Estudar para apresentação do MVP"),
        Task("Configurar API da IA")
    )

    fun addTask(name: String) {
        taskList.add(Task(name = name))
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

                val request = OllamaRequest(model = "gemma2:2b", prompt = prompt)
                val result = OllamaClient.api.generateFeedback(request)

                task.aiFeedback = result.response
            } catch (e: Exception) {
                task.aiFeedback = "Erro ao conectar com a IA: ${e.message}. O Ollama está rodando no PC?"
            } finally {
                task.isLoadingFeedback = false
            }
        }
    }
}