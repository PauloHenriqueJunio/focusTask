package com.skynet.focustask.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // Import novo!
import kotlinx.coroutines.delay // Import novo!
import kotlinx.coroutines.launch // Import novo!

class Task(val name: String) {
    var timeElapsed by mutableIntStateOf(0)
    var isRunning by mutableStateOf(false)
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

    // A MÁGICA ACONTECE AQUI: O Motor do tempo agora vive no Cérebro!
    fun toggleTimer(task: Task) {
        task.isRunning = !task.isRunning // Inverte o Play/Pause

        if (task.isRunning) {
            // Isso aqui roda "em background" protegido pelo ViewModel
            viewModelScope.launch {
                while (task.isRunning) {
                    delay(1000L)
                    task.timeElapsed++
                }
            }
        }
    }
}