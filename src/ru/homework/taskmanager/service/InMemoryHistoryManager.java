package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.model.Node;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();
    private final Map<Integer, Node<Task>> nodeMap = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;

    private void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail != null) {
            oldTail.next = newNode;
        } else {
            head = newNode;
        }
        nodeMap.put(element.getId(), newNode);
    }

    private void getTasks() {
        history.clear();
        Node<Task> node = head;
        while (node != null) {
            history.add(node.data);
            node = node.next;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        nodeMap.remove(node.data.getId());
    }

    @Override
    public void add(Task task) { //проверка на null была реализована в трех методах получения по идентификатору,оставил закомментированной
        if (task == null) {
            return;
        }

        if (nodeMap.get(task.getId()) != null) {
            removeNode(nodeMap.get(task.getId()));//удаляем однажды просмотренную задачу с тем же id
        }
        linkLast(task);//добавляем просмотренную задачу в конец списка
    }

    @Override
    public void remove(int id) {
        if (nodeMap.get(id) != null) {
            removeNode(nodeMap.get(id));//удаляем задачу из истории
        }
    }

    @Override
    public List<Task> getHistory() {
        getTasks(); //перекладываем задачи из связного списка в ArrayList
        return List.copyOf(history);
    }

}
