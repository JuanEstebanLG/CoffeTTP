package com.javaPractice.infra.Exception;

public class EditorNotFoundException extends RuntimeException {
    public EditorNotFoundException(String message) {
        super(message);
    }
}
