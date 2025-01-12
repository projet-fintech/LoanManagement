package com.project.loan_management.services;

import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PredictionServiceImpl implements PredictionService {

    private static final String PREDICTION_API_URL = "http://127.0.0.1:5000/predict_api";

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getPredictionAndUpdateResult(Long applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("LoanApplication with ID " + applicationId + " not found"));

        // Create a LinkedHashMap to preserve feature order as expected by the Flask model
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("person_age", loanApplication.getPersonAge());
        data.put("person_gender", mapGenderToCode(loanApplication.getPersonGender()));
        data.put("person_education", mapEducationToCode(loanApplication.getPersonEducation()));
        data.put("person_income", loanApplication.getPersonIncome());
        data.put("person_emp_exp", loanApplication.getPersonEmpExp());
        data.put("person_home_ownership", mapHomeOwnershipToCode(loanApplication.getPersonHomeOwnership()));
        data.put("loan_amnt", loanApplication.getLoanAmount()); // Changed from "loan_amnt" to "loan_amount"
        data.put("loan_intent", mapLoanIntentToCode(loanApplication.getLoanIntent()));
        data.put("loan_int_rate", loanApplication.getLoanIntRate());
        data.put("loan_percent_income", loanApplication.getLoanPercentIncome());
        data.put("cb_person_cred_hist_length", loanApplication.getCbPersonCredHistLength());
        data.put("credit_score", loanApplication.getCreditScore());
        data.put("previous_loan_defaults_on_file", loanApplication.getPreviousLoanDefaultsOnFile() ? 1 : 0);

        // Wrap the data under the "data" key as expected by the Flask API
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("data", data);

        try {
            // Make the POST request
            Map<String, String> response = restTemplate.postForObject(PREDICTION_API_URL, requestData, Map.class);

            if (response == null || !response.containsKey("prediction")) {
                throw new RuntimeException("Invalid response from Flask API: " + response);
            }

            String predictionResult = response.get("prediction");
            loanApplication.setPredictionResult(predictionResult);
            loanApplicationRepository.save(loanApplication);

            return predictionResult;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get prediction from Flask API", e);
        }
    }

    // Map gender to numeric codes
    private int mapGenderToCode(String gender) {
        return "Female".equalsIgnoreCase(gender) ? 0 : 1;
    }

    // Map education level to numeric codes
    private int mapEducationToCode(String education) {
        return switch (education.toLowerCase()) {
            case "bachelor" -> 0;
            case "associate" -> 1;
            case "high school" -> 2;
            case "master" -> 3;
            case "doctorate" -> 4;
            default -> throw new IllegalArgumentException("Invalid education level: " + education);
        };
    }

    // Map home ownership to numeric codes
    private int mapHomeOwnershipToCode(String homeOwnership) {
        return switch (homeOwnership.toLowerCase()) {
            case "rent" -> 0;
            case "mortgage" -> 1;
            case "own" -> 2;
            case "other" -> 3;
            default -> throw new IllegalArgumentException("Invalid home ownership: " + homeOwnership);
        };
    }

    // Map loan intent to numeric codes
    private int mapLoanIntentToCode(String intent) {
        return switch (intent.toLowerCase()) {
            case "education" -> 0;
            case "medical" -> 1;
            case "venture" -> 2;
            case "personal" -> 3;
            case "debt consolidation" -> 4;
            case "home improvement" -> 5;
            default -> throw new IllegalArgumentException("Invalid loan intent: " + intent);
        };
    }
}
