package com.project.loan_management.services;

import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.entities.LoanStatus;
import com.project.loan_management.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        loanApplication.setApplicationDate(java.time.LocalDate.now());
        loanApplication.setStatus(LoanStatus.PENDING); // Assuming default status
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public Optional<LoanApplication> getLoanApplicationById(Long id) {
        return loanApplicationRepository.findById(id);
    }

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    @Override
    public LoanApplication updateLoanApplication(Long id, LoanApplication loanApplication) {
        return loanApplicationRepository.findById(id)
                .map(existingLoan -> {
                    existingLoan.setPersonAge(loanApplication.getPersonAge());
                    existingLoan.setPersonGender(loanApplication.getPersonGender());
                    existingLoan.setPersonEducation(loanApplication.getPersonEducation());
                    existingLoan.setPersonIncome(loanApplication.getPersonIncome());
                    existingLoan.setPersonEmpExp(loanApplication.getPersonEmpExp());
                    existingLoan.setPersonHomeOwnership(loanApplication.getPersonHomeOwnership());
                    existingLoan.setLoanAmount(loanApplication.getLoanAmount());
                    existingLoan.setLoanIntent(loanApplication.getLoanIntent());
                    existingLoan.setLoanIntRate(loanApplication.getLoanIntRate());
                    existingLoan.setLoanPercentIncome(loanApplication.getLoanPercentIncome());
                    existingLoan.setCbPersonCredHistLength(loanApplication.getCbPersonCredHistLength());
                    existingLoan.setCreditScore(loanApplication.getCreditScore());
                    existingLoan.setPreviousLoanDefaultsOnFile(loanApplication.getPreviousLoanDefaultsOnFile());
                    existingLoan.setStatus(loanApplication.getStatus());
                    existingLoan.setPredictionResult(loanApplication.getPredictionResult());
                    return loanApplicationRepository.save(existingLoan);
                })
                .orElseThrow(() -> new RuntimeException("LoanApplication with ID " + id + " not found"));
    }

    @Override
    public void deleteLoanApplication(Long id) {
        if (loanApplicationRepository.existsById(id)) {
            loanApplicationRepository.deleteById(id);
        } else {
            throw new RuntimeException("LoanApplication with ID " + id + " not found");
        }
    }

    @Override
    public List<LoanApplication> getLoanApplicationsByUserId(UUID userId) {
            return loanApplicationRepository.findByUserId(userId);
    }
}
