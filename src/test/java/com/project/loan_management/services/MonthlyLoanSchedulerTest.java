package com.project.loan_management.services;

import com.banque.events.dto.MonthlyLoanPaying;
import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.entities.LoanStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyLoanSchedulerTest {

    @Mock
    private LoanApplicationService loanApplicationService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private MonthlyLoanScheduler monthlyLoanScheduler;

    @Test
    void processMonthlyPayments_should_send_payment_for_approved_loans() {

        LoanApplication loan1 = new LoanApplication();
        loan1.setId(1L);
        loan1.setAccount_id(UUID.randomUUID());
        loan1.setStatus(LoanStatus.APPROVED);
        loan1.setMonthlyPaying(500.00);

        LoanApplication loan2 = new LoanApplication();
        loan2.setId(2L);
        loan2.setAccount_id(UUID.randomUUID());
        loan2.setStatus(LoanStatus.APPROVED);
        loan2.setMonthlyPaying(1000.00);


        when(loanApplicationService.getAllLoanApplications()).thenReturn(Arrays.asList(loan1, loan2));
        MonthlyLoanPaying request1 = new MonthlyLoanPaying(loan1.getAccount_id(), loan1.getMonthlyPaying());
        MonthlyLoanPaying request2 = new MonthlyLoanPaying(loan2.getAccount_id(), loan2.getMonthlyPaying());

        monthlyLoanScheduler.processMonthlyPayments();

        verify(transactionService, times(1)).sendPaymentRequest(request1);
        verify(transactionService, times(1)).sendPaymentRequest(request2);
    }

    @Test
    void processMonthlyPayments_should_not_send_payment_when_no_approved_loan(){
        when(loanApplicationService.getAllLoanApplications()).thenReturn(Arrays.asList());

        monthlyLoanScheduler.processMonthlyPayments();

        verify(transactionService, times(0)).sendPaymentRequest(any());
    }
}