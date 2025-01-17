package com.project.loan_management.services;

import com.banque.events.dto.MonthlyLoanPaying;

public interface TransactionService {
    void sendPaymentRequest(MonthlyLoanPaying request);
}
