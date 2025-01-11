package com.project.loan_management.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanId;

    private String gender;

    private String married;

    private String dependents;

    private String education;

    private String selfEmployed;

    private Integer applicantIncome;

    private Integer coapplicantIncome;

    private Integer loanAmount;

    private Integer loanAmountTerm;

    private Integer creditHistory;

    private String propertyArea;

    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private String predictionResult;

    private  Long UserId ;

}
