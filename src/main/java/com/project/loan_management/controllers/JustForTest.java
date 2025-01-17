package com.project.loan_management.controllers;

import com.project.loan_management.services.MonthlyLoanScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class JustForTest {
    @Autowired
    private MonthlyLoanScheduler monthlyLoanScheduler;

    @GetMapping("/pay")
    public String testerPayement() {
        monthlyLoanScheduler.processMonthlyPayments();
        return "payment done";
    }
}
