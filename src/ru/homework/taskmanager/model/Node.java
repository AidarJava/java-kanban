package ru.homework.taskmanager.model;

public class Node<T> {

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", prev=" + (prev != null ? prev.data : "null") +
                ", next=" + (next != null ? next.data : "null") +
                '}';
    }
}