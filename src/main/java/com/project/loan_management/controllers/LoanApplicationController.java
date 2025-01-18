package com.project.loan_management.controllers;

import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.services.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan-applications")
@CrossOrigin(origins = "*")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    // Create Loan Application
    @PostMapping
    public ResponseEntity<LoanApplication> createLoanApplication(@RequestBody LoanApplication loanApplication) {
        LoanApplication createdLoan = loanApplicationService.createLoanApplication(loanApplication);
        return ResponseEntity.ok(createdLoan);
    }

    // Get Loan Application by ID
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getLoanApplicationById(@PathVariable Long id) {
        return loanApplicationService.getLoanApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get All Loan Applications
    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllLoanApplications() {
        List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplications();
        return ResponseEntity.ok(loanApplications);
    }

    // Update Loan Application
    @PutMapping("/{id}")
    public ResponseEntity<LoanApplication> updateLoanApplication(
            @PathVariable Long id,
            @RequestBody LoanApplication loanApplication) {
        try {
            LoanApplication updatedLoan = loanApplicationService.updateLoanApplication(id, loanApplication);
            return ResponseEntity.ok(updatedLoan);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete Loan Application
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanApplication(@PathVariable Long id) {
        try {
            loanApplicationService.deleteLoanApplication(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get Loan Applications by Client ID
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<LoanApplication>> getLoanApplicationsByUserId(@PathVariable UUID userId) {
        List<LoanApplication> loanApplications = loanApplicationService.getLoanApplicationsByUserId(userId);
        return ResponseEntity.ok(loanApplications);
    }
}
