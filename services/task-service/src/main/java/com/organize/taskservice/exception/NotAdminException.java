package com.organize.taskservice.exception;

public class NotAdminException extends Exception {
    public NotAdminException(String message){
        super(message);
    }
}
