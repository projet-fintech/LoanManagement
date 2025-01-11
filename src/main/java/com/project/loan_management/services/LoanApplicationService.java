package com.project.loan_management.services;

import com.project.loan_management.entities.LoanApplication;
import java.util.List;
import java.util.Optional;

public interface LoanApplicationService {

    LoanApplication createLoanApplication(LoanApplication loanApplication);

    Optional<LoanApplication> getLoanApplicationById(Long id);

    List<LoanApplication> getAllLoanApplications();

    LoanApplication updateLoanApplication(Long id, LoanApplication loanApplication);

    void deleteLoanApplication(Long id);
}
