# FocusTask 

Um aplicativo moderno de gestão de tempo e tarefas focado em produtividade. Desenvolvido nativamente para Android utilizando as ferramentas e arquiteturas mais recentes do ecossistema Google.

O objetivo do FocusTask é ir além de um simples "To-Do List", oferecendo monitoramento de tempo em tempo real e, futuramente, análises inteligentes sobre o desempenho do usuário.

## Funcionalidades Atuais

* **Gestão de Tarefas:** Criação rápida de atividades em uma interface fluida.
* **Cronômetro Sincronizado:** Rastreamento de tempo individual por tarefa.
* **Estado Global:** O relógio continua rodando e mantém a precisão mesmo ao navegar entre diferentes telas do aplicativo.
* **UI/UX Moderna:** Interface limpa e responsiva construída com os componentes e diretrizes do Material Design 3.
* **Dark Mode Ready:** Cores dinâmicas que se adaptam automaticamente ao tema do sistema operacional do usuário.

## Tecnologias e Arquitetura

Este projeto foi construído utilizando as melhores práticas do desenvolvimento Android moderno:

* **[Kotlin](https://kotlinlang.org/):** Linguagem de programação oficial.
* **[Jetpack Compose](https://developer.android.com/jetpack/compose):** Toolkit declarativo moderno para construção de UI nativa (substituindo o antigo XML).
* **Arquitetura MVVM (Model-View-ViewModel):** Separação clara de responsabilidades entre a interface visual e a lógica de negócios/estado.
* **Coroutines & ViewModelScope:** Gerenciamento eficiente de tarefas em background e loops de tempo sem travar a interface.
* **Jetpack Navigation Compose:** Roteamento e navegação fluida entre telas com passagem de parâmetros.

## 📱 Como executar o projeto

1. Clone este repositório:
   ```bash
   git clone [https://github.com/PauloHenriqueJunio/focusTask.git](https://github.com/PauloHenriqueJunio/focusTask.git)
* Abra o projeto no Android Studio.

* Aguarde o Gradle Sync finalizar para baixar todas as dependências.

* Execute o projeto em um Emulador ou Dispositivo Físico clicando em Run (Shift + F10).