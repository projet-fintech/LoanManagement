package com.project.loan_management.services;

import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.entities.LoanStatus;
import com.project.loan_management.repositories.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceImplTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;

    private LoanApplication loanApplication;

    @BeforeEach
    void setUp() {
        loanApplication = new LoanApplication();
        loanApplication.setId(1L);
        loanApplication.setUserId(UUID.randomUUID());
    }

    @Test
    void createLoanApplication_should_return_created_loan_with_pending_status() {
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(loanApplication);

        LoanApplication createdLoan = loanApplicationService.createLoanApplication(loanApplication);

        assertNotNull(createdLoan);
        assertEquals(LoanStatus.PENDING, createdLoan.getStatus());
        assertEquals(LocalDate.now(),createdLoan.getApplicationDate());
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }
    @Test
    void getLoanApplicationById_should_return_loan_when_exist(){
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(loanApplication));
        Optional<LoanApplication> getLoan =  loanApplicationService.getLoanApplicationById(1L);
        assertTrue(getLoan.isPresent());
        assertEquals(loanApplication.getId(),getLoan.get().getId());
        verify(loanApplicationRepository, times(1)).findById(1L);
    }

    @Test
    void getLoanApplicationById_should_return_empty_when_not_exist(){
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<LoanApplication> getLoan =  loanApplicationService.getLoanApplicationById(1L);
        assertFalse(getLoan.isPresent());
        verify(loanApplicationRepository, times(1)).findById(1L);
    }
    @Test
    void getAllLoanApplications_should_return_list_of_loans() {
        when(loanApplicationRepository.findAll()).thenReturn(Arrays.asList(loanApplication,new LoanApplication()));
        List<LoanApplication> listLoans = loanApplicationService.getAllLoanApplications();
        assertEquals(2, listLoans.size());
        verify(loanApplicationRepository, times(1)).findAll();
    }

    @Test
    void updateLoanApplication_should_return_updated_loan() {
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(loanApplication));

        LoanApplication newLoan = new LoanApplication();
        newLoan.setPersonAge(32);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(newLoan);

        LoanApplication updatedLoan = loanApplicationService.updateLoanApplication(1L, newLoan);
        assertNotNull(updatedLoan);
        assertEquals(32, updatedLoan.getPersonAge());
        verify(loanApplicationRepository, times(1)).findById(1L);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    void updateLoanApplication_should_throw_exception_when_not_exist() {
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanApplicationService.updateLoanApplication(1L, new LoanApplication());
        });
        assertEquals("LoanApplication with ID 1 not found", exception.getMessage());
        verify(loanApplicationRepository, times(1)).findById(1L);
    }

    @Test
    void deleteLoanApplication_should_delete_loan() {
        when(loanApplicationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(loanApplicationRepository).deleteById(1L);

        loanApplicationService.deleteLoanApplication(1L);
        verify(loanApplicationRepository, times(1)).deleteById(1L);
        verify(loanApplicationRepository, times(1)).existsById(1L);
    }
    @Test
    void deleteLoanApplication_should_throw_exception_when_not_exist() {
        when(loanApplicationRepository.existsById(1L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanApplicationService.deleteLoanApplication(1L);
        });
        assertEquals("LoanApplication with ID 1 not found", exception.getMessage());
        verify(loanApplicationRepository, times(1)).existsById(1L);
    }
    @Test
    void getLoanApplicationsByUserId_should_return_list_of_loan_for_user() {
        UUID userId =  UUID.randomUUID();
        when(loanApplicationRepository.findByUserId(userId)).thenReturn(Arrays.asList(loanApplication, new LoanApplication()));

        List<LoanApplication> loanList = loanApplicationService.getLoanApplicationsByUserId(userId);

        assertEquals(2, loanList.size());
        verify(loanApplicationRepository, times(1)).findByUserId(userId);
    }
}