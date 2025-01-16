package com.project.loan_management.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents a loan application with user details and associated loan data.
 */
@Entity
@Data
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the loan application

    private Integer personAge; // Age of the applicant

    private String personGender; // Gender of the applicant ("Male", "Female")

    private String personEducation; // Educational level of the applicant ("Bachelor", "Master","Associate" , "High School" , "Master" , "Doctorate")

    private Double personIncome; // Annual income of the applicant

    private Integer personEmpExp; // Employment experience in years

    private String personHomeOwnership; // Home ownership status ("Rent", "Own", "Mortgage", "Others")

    private Double loanAmount; // Requested loan amount

    private String loanIntent; // Purpose of the loan ("Personal", "Debt Consolidation" "Education", "Medical" ,  "Venture" , "Home improvement" )

    private Double loanIntRate; // Interest rate for the loan

    private Double loanPercentIncome; // Percentage of income allocated to the loan

    private Integer cbPersonCredHistLength; // Length of credit history in years

    private Integer creditScore; // Credit score of the applicant

    private Boolean previousLoanDefaultsOnFile; // Indicates if there are previous loan defaults (true/false)

    private LocalDate applicationDate; // Date when the application was submitted

    @Enumerated(EnumType.STRING)
    private LoanStatus status; // Status of the loan application (e.g., PENDING, APPROVED)

    private String predictionResult; // Prediction result from the Flask API

    private Long userId; // ID of the user associated with this application

    private Double monthlyPaying; // Persisted monthly payment field

    /**
     * Method to calculate and set the monthly payment based on loanAmount and loanIntRate.
     */
    public void calculateMonthlyPaying() {
        if (loanAmount != null && loanIntRate != null) {
            this.monthlyPaying = (loanAmount + (loanAmount * (loanIntRate / 100))) / 12;
        } else {
            this.monthlyPaying = null; // Set to null if inputs are missing
        }
    }

    /**
     * Lifecycle callback to calculate monthlyPaying before persisting or updating the entity.
     */
    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        calculateMonthlyPaying();
    }
}
