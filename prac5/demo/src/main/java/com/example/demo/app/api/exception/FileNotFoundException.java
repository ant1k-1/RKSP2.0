package com.example.demo.app.api.exception;

public class FileNotFoundException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Файл с id = %s не найден";
    public FileNotFoundException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
