# FocusTask 

Um aplicativo moderno de gestão de tempo e tarefas focado em produtividade. Desenvolvido nativamente para Android utilizando as ferramentas e arquiteturas mais recentes do ecossistema Google.

O objetivo do FocusTask é ir além de um simples "To-Do List", oferecendo monitoramento de tempo em tempo real e, futuramente, análises inteligentes sobre o desempenho do usuário.

## Funcionalidades Atuais

### 🎯 Gestão Inteligente de Tarefas
* **Criação Completa de Tarefas:** Adicione tarefas com informações detalhadas:
  - Nome da tarefa
  - Prazo (Hoje, Amanhã, 3 dias, 1 semana, 2 semanas, 1 mês, Sem prazo)
  - Dificuldade (Fácil, Média, Difícil)
  - Tempo estimado (30min, 1h, 2h, 3h, 4h, 1 dia, 2 dias, 3 dias, 1 semana, 2 semanas)

### 🧠 Organização Automática com IA
* **Reorganização Inteligente:** A IA local analisa suas tarefas e as reorganiza automaticamente considerando:
  - Urgência do prazo
  - Nível de dificuldade
  - Tempo estimado de execução
  - Equilíbrio entre tarefas rápidas e longas
* **Privacidade Total:** Processamento 100% local usando Ollama (sem enviar dados para nuvem)

### ⏱️ Monitoramento de Tempo
* **Cronômetro Sincronizado:** Rastreamento de tempo individual por tarefa em tempo real
* **Estado Global:** O relógio continua rodando e mantém a precisão mesmo ao navegar entre diferentes telas do aplicativo

### 🤖 Feedback Inteligente
* **Análise de Desempenho:** Ao finalizar uma tarefa, a IA gera feedback personalizado e encorajador sobre seu desempenho
* **Sugestões Contextualizadas:** Receba insights sobre produtividade baseados no tempo gasto

### 🎨 UI/UX Moderna
* **Interface Limpa:** Construída com os componentes e diretrizes do Material Design 3
* **Dark Mode Ready:** Cores dinâmicas que se adaptam automaticamente ao tema do sistema operacional
* **Navegação Fluida:** Transições suaves entre telas com preview de tarefas

## Tecnologias e Arquitetura

Este projeto foi construído utilizando as melhores práticas do desenvolvimento Android moderno:

* **[Kotlin](https://kotlinlang.org/):** Linguagem de programação oficial.
* **[Jetpack Compose](https://developer.android.com/jetpack/compose):** Toolkit declarativo moderno para construção de UI nativa (substituindo o antigo XML).
* **Arquitetura MVVM (Model-View-ViewModel):** Separação clara de responsabilidades entre a interface visual e a lógica de negócios/estado.
* **Coroutines & ViewModelScope:** Gerenciamento eficiente de tarefas em background e loops de tempo sem travar a interface.
* **Jetpack Navigation Compose:** Roteamento e navegação fluida entre telas com passagem de parâmetros.
* **[Retrofit](https://square.github.io/retrofit/):** Cliente HTTP para comunicação com a API local do Ollama.
* **[Ollama](https://ollama.ai/):** Runtime local de modelos de IA para processar análises sem dependência de nuvem.
* **Gemma 3 (1B):** Modelo de linguagem otimizado para execução local e análise de produtividade.

## 🧠 Fluxo de IA no App

```
Usuário adiciona/edita tarefas com prazos e tempo estimado
                    ↓
App constrói prompt contextual com todas as informações
                    ↓
Ollama processa localmente usando Gemma 3:1b
                    ↓
IA retorna ordem otimizada ou feedback de desempenho
                    ↓
App reorganiza tarefas automaticamente ou exibe análise
```

### Vantagens da IA Local
✅ **Funciona offline** - Sem necessidade de internet  
✅ **Privacidade total** - Nenhum dado sai do dispositivo  
✅ **Sem custo** - Não há cobranças de API  
✅ **Resposta rápida** - Processamento instantâneo  
✅ **Personalizável** - Você controla o modelo utilizado

## ⚙️ Requisitos e Configuração

### Requisitos do Sistema
- **Android Studio** (última versão recomendada)
- **JDK 11 ou superior**
- **Emulador Android** ou dispositivo físico com depuração USB ativada
- **[Ollama](https://ollama.ai/)** instalado no seu PC (para funcionalidades de IA)

### Configuração do Ollama

1. **Instale o Ollama** no seu PC:
   ```bash
   # Linux / macOS
   curl https://ollama.ai/install.sh | sh
   ```

2. **Baixe o modelo Gemma 3:1b**:
   ```bash
   ollama pull gemma3:1b
   ```

3. **Inicie o servidor Ollama**:
   ```bash
   ollama serve
   ```

4. **Verifique a instalação**:
   ```bash
   ollama list
   # Deve mostrar gemma3:1b na lista
   ```

> **Nota:** O app usa `http://10.0.2.2:11434/` para conectar ao Ollama quando rodando no emulador Android. Se usar dispositivo físico, altere para o IP local do seu PC em [OllamaApi.kt](app/src/main/java/com/skynet/focustask/OllamaApi.kt).

## 📱 Como executar o projeto

1. **Clone este repositório:**
   ```bash
   git clone https://github.com/PauloHenriqueJunio/focusTask.git
   cd focusTask
   ```

2. **Abra o projeto no Android Studio**

3. **Aguarde o Gradle Sync** finalizar para baixar todas as dependências

4. **Configure o Ollama** (veja seção acima)

5. **Execute o projeto:**
   - Inicie um Emulador ou conecte um dispositivo físico
   - Clique em **Run** (`Shift + F10`)

## 🚀 Como usar

### Adicionando Tarefas
1. Digite o nome da tarefa no campo de input
2. Clique no botão **+**
3. Configure os detalhes:
   - Selecione o **prazo** (de "Hoje" até "1 mês")
   - Escolha a **dificuldade** (Fácil, Média ou Difícil)
   - Defina o **tempo estimado** (de 30min até 2 semanas)
4. Clique em **Adicionar**

### Organizando com IA
1. Adicione pelo menos 2 tarefas
2. Clique no botão **🧠 Organizar**
3. A IA analisará e reorganizará suas tarefas automaticamente
4. As tarefas mais urgentes e prioritárias ficarão no topo

### Gerenciando o Tempo
1. Clique no ícone ▶️ no card da tarefa para iniciar o cronômetro
2. Clique em ⏸️ para pausar
3. Clique no card para ver detalhes da tarefa
4. Na tela de detalhes, clique em **Finalizar** para receber feedback da IA

## 🎯 Roadmap Futuro

- [ ] Persistência de dados com Room Database
- [ ] Estatísticas e gráficos de produtividade
- [ ] Notificações de prazos próximos
- [ ] Widgets para tela inicial
- [ ] Sugestões proativas da IA
- [ ] Detecção de procrastinação
- [ ] Divisão automática de tarefas grandes
- [ ] Export/import de tarefas

## 📄 Licença

Este projeto está sob desenvolvimento acadêmico.

---

Desenvolvido com ❤️ e ☕ por [Paulo Henrique Junio](https://github.com/PauloHenriqueJunio)