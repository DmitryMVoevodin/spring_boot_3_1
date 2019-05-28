package com.netcracker.task_3_1.calculator;

public class NotValidException extends Exception {
    @Override
    public String toString() {
        return "Вводимые параметры невалидные";
    }
}