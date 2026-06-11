package com.prachi18.college_management_system.Exceptions;

public class StudentAlreadyEnrolledException extends RuntimeException{
    public StudentAlreadyEnrolledException(String message) {
        super(message);
    }
}
