package com.project.loan_management.exceptions;

public class TransactionServiceException extends RuntimeException {
    public TransactionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionServiceException(String message) {
        super(message);
    }
}