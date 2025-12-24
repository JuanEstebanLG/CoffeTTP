package com.javaPractice.infra.Exception;

public class EditorCloseWithErrorException extends RuntimeException {
    public EditorCloseWithErrorException(String message) {
        super(message);
    }
}
