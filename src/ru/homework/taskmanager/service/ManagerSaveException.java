package ru.homework.taskmanager.service;

public class ManagerSaveException extends RuntimeException {
    ManagerSaveException(String msg, Exception e) {
        super(msg, e);
    }
}
