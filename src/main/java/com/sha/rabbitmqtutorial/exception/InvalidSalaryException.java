package com.sha.rabbitmqtutorial.exception;

public class InvalidSalaryException extends RuntimeException {

    public InvalidSalaryException() {
        super("salary invalid");
    }
}
