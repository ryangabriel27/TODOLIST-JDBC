package todolist.Control;

import java.awt.image.DataBufferInt;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import todolist.Model.Task;

public class TodolistControl {
    private List<Task> tasks;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    // Construtor
    public TodolistControl(List<Task> tasks, JList<String> taskList, DefaultListModel<String> listModel) {
        this.tasks = tasks;
        this.taskList = taskList;
        this.listModel = listModel;
    }

    // Métodos :
    public void addTask(String taskDescription) {
        // Adiciona uma nova task à lista de tasks
        if (!taskDescription.isEmpty() && !taskDescription.equals("Digite sua tarefa...")) { // Verifica se o campo não
                                                                                             // está vazio , caso não
                                                                                             // esteja adiciona uma task
                                                                                             // a lista
            LocalDateTime now = LocalDateTime.now();
            Task newTask = new Task(String.valueOf(now), taskDescription);
            tasks.add(newTask);
            new TodolistDAO().cadastrar(taskDescription, String.valueOf(now));
            JOptionPane.showMessageDialog(null, "Tarefa adicionada", "TODO-LIST", JOptionPane.INFORMATION_MESSAGE); // Notifica
                                                                                                                    // o
                                                                                                                    // usuário
                                                                                                                    // que
                                                                                                                    // uma
                                                                                                                    // task
                                                                                                                    // foi
                                                                                                                    // adicionada
            updateTaskList();
        }
    }

    public void deleteTask(int tarefaSelecionada) {
        // Exclui a task selecionada da lista de tasks
        if (tarefaSelecionada >= 0 && tarefaSelecionada < tasks.size()) {

            int resposta = JOptionPane.showConfirmDialog(null, "Você deseja excluir essa tarefa?", "TODO-LIST",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE); // Pergunta ao usuário se ele realmente
                                                                           // deseja excluir a task

            if (resposta == JOptionPane.YES_OPTION) {// Caso escolha a opção 'YES', a tarefa é excluída
                new TodolistDAO().apagar((tarefaSelecionada + 1));
                tasks.remove(tarefaSelecionada);
                JOptionPane.showMessageDialog(null, "Tarefa excluída", "TODO-LIST", JOptionPane.INFORMATION_MESSAGE); // Notifica
                                                                                                                      // o
                                                                                                                      // usuário
                                                                                                                      // de
                                                                                                                      // que
                                                                                                                      // a
                                                                                                                      // tarefa
                                                                                                                      // foi
                                                                                                                      // excluída
                updateTaskList();
            }

        }
    }

    public void markTaskDone(int tarefaSelecionada) {
        // Marca a task selecionada como concluída
        if (tarefaSelecionada >= 0 && tarefaSelecionada < tasks.size()) {

            int resposta = JOptionPane.showConfirmDialog(null, "Você concluiu essa tarefa?", "TODO-LIST",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // Pergunta ao usuário se ele concluiu a
                                                                              // tarefa

            if (resposta == JOptionPane.YES_OPTION) { // Caso escolha 'YES' , a tarefa é marcada como concluída
                Task task = tasks.get(tarefaSelecionada);

                JOptionPane.showMessageDialog(null, "Tarefa concluída", "TODO-LIST", JOptionPane.INFORMATION_MESSAGE); // Notifica
                                                                                                                       // o
                                                                                                                       // usuário
                                                                                                                       // de
                                                                                                                       // que
                                                                                                                       // a
                                                                                                                       // tarefa
                                                                                                                       // foi
                                                                                                                       // concluída
                task.setDone(true);
                updateTaskList();
            }
        }
    }

    public void filterTasks(JComboBox<String> filterComboBox) {
        // Filtra as tasks com base na seleção do JComboBox
        String filter = (String) filterComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tasks) {
            if (filter.equals("Todas") || (filter.equals("Ativas") &&
                    !task.isDone()) || (filter.equals("Concluídas") && task.isDone())) {
                listModel.addElement(task.getNome());
            }
        }
    }

    public void clearCompletedTasks() {
        // Limpa todas as tasks concluídas da lista
        int resposta = JOptionPane.showConfirmDialog(null, "Você deseja excluir todas as tarefas concluídas?",
                "TODO-LIST",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); // Pergunta ao usuário se ele quer excluir
                                                                         // todas
                                                                         // as tarefas concluídas

        if (resposta == JOptionPane.YES_OPTION) { // Caso escolha 'YES', todas as tarefas concluídas são apagadas
            List<Task> completedTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isDone()) {
                    completedTasks.add(task);
                }
            }
            tasks.removeAll(completedTasks);
            updateTaskList();
        }
    }

    public void updateTaskList() {
        // Atualiza a lista de tasks exibida na GUI
        listModel.clear();
        tasks = new TodolistDAO().listarTodos();
        for (Task task : tasks) {
            listModel.addElement(task.getNome() + (task.isDone() ? " (Concluída)" : ""));
        }
    }
}
