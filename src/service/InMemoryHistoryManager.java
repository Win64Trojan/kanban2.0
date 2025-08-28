package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head = null;
    private Node<Task> tail = null;
    private final Map<Integer, Node<Task>> navigatorTasks = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task.getId() != null) {
            remove(task.getId());
        }

        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            linkLast(epic);
            return;
        } else if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            linkLast(subTask);
            return;
        }

        linkLast(task);
    }

    @Override
    public void remove(int id) {
        remiveNode(navigatorTasks.get(id));

    }

    private void remiveNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        if (node == head) {
            head = node.getNext();
        } else {
            node.getPrev().setNext(node.getNext());
        }

        if (node == tail) {
            tail = node.getPrev();
        } else {
            node.getNext().setPrev(node.getPrev());
        }

        navigatorTasks.remove(node.getTask().getId());
    }

    public void linkLast(Task task) {

        final Node<Task> newNode = new Node<>(null, task, null);

        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }
        tail = newNode;
        navigatorTasks.put(tail.getTask().getId(), tail);
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (isEmpty()) {
            System.out.println("No history found");
            return null;
        }
        return getTasks();


    }

    public void removeHistory() {
        head = null;
        tail = null;
        navigatorTasks.clear();
    }

    private boolean isEmpty() {
        return head == null;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasksList.add(currentNode.getTask());
            currentNode = currentNode.getNext();
        }

        return tasksList;
    }
}

