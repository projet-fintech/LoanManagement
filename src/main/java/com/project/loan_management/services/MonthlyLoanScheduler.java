package com.project.loan_management.services;

import com.banque.events.dto.MonthlyLoanPaying;
import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.entities.LoanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthlyLoanScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyLoanScheduler.class);

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired TransactionService transactionService;

    //@Scheduled(cron = "0 0 0 1 * ?") // Runs every month at midnight
    public void processMonthlyPayments() {
        logger.info("Starting monthly payment processing for approved loans");
        List<LoanApplication> approvedLoans = loanApplicationService.getAllLoanApplications().stream()
                .filter(loan -> loan.getStatus() == LoanStatus.APPROVED)
                .toList();
        if (approvedLoans.isEmpty()) {
            logger.info("No approved loans to process.");
            return;
        }
        for (LoanApplication loan : approvedLoans) {
            try {
                MonthlyLoanPaying request = new MonthlyLoanPaying(loan.getAccount_id(), loan.getMonthlyPaying());
                transactionService.sendPaymentRequest(request);
                logger.info("Payment request sent for loan ID: {}, account ID: {}, amount: {}", loan.getId(), loan.getAccount_id(), loan.getMonthlyPaying());
            } catch (Exception e) {
                logger.error("Failed to send payement request for loan ID: {}", loan.getId(),e);
            }
        }
        logger.info("Completed monthly payement processing");
    }
}
