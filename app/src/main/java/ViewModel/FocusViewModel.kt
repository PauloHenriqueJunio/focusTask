package ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 1. O nosso Modelo de Dados (A Tarefa)
// Transformamos a tarefa de uma simples String para um objeto inteligente!
// Agora cada tarefa tem seu próprio nome, seu próprio tempo e sabe se está rodando.
class Task(val name: String) {
    var timeElapsed by mutableIntStateOf(0)
    var isRunning by mutableStateOf(false)
}

// 2. O nosso Gerenciador de Estado Global (O "Context API/Zustand" do Android)
class FocusViewModel : ViewModel() {

    // Nossa lista global de tarefas (Substitui aquela lista fixa que estava na Tela 1)
    val taskList = mutableStateListOf(
        Task("Estudar para apresentação do MVP"),
        Task("Configurar API da IA")
    )

    // Função para adicionar nova tarefa
    fun addTask(name: String) {
        taskList.add(Task(name = name))
    }

    // Função para buscar uma tarefa específica (A Tela 2 vai usar isso!)
    fun getTaskByName(name: String): Task? {
        return taskList.find { it.name == name }
    }
}