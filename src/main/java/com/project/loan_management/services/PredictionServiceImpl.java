package com.project.loan_management.services;

import com.project.loan_management.entities.LoanApplication;
import com.project.loan_management.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PredictionServiceImpl implements PredictionService {

    private static final String PREDICTION_API_URL = "http://127.0.0.1:5000/predict_api";

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String getPredictionAndUpdateResult(Long loanApplicationId) {
        // Step 1: Fetch the loan application by ID
        Optional<LoanApplication> loanApplicationOptional = loanApplicationRepository.findById(loanApplicationId);

        if (loanApplicationOptional.isEmpty()) {
            throw new RuntimeException("LoanApplication with ID " + loanApplicationId + " not found");
        }

        LoanApplication loanApplication = loanApplicationOptional.get();

        // Step 2: Prepare the request data
        Map<String, Object> requestData = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("Loan_ID", loanApplication.getLoanId());
        data.put("Gender", loanApplication.getGender());
        data.put("Married", loanApplication.getMarried());
        data.put("Dependents", loanApplication.getDependents());
        data.put("Education", loanApplication.getEducation());
        data.put("Self_Employed", loanApplication.getSelfEmployed());
        data.put("ApplicantIncome", loanApplication.getApplicantIncome());
        data.put("CoapplicantIncome", loanApplication.getCoapplicantIncome());
        data.put("LoanAmount", loanApplication.getLoanAmount());
        data.put("Loan_Amount_Term", loanApplication.getLoanAmountTerm());
        data.put("Credit_History", loanApplication.getCreditHistory());
        data.put("Property_Area", loanApplication.getPropertyArea());
        requestData.put("data", data);

        // Step 3: Send the POST request to the Flask model
        Map<String, String> response = restTemplate.postForObject(PREDICTION_API_URL, requestData, Map.class);

        if (response == null || !response.containsKey("prediction")) {
            throw new RuntimeException("Failed to get prediction from Flask API");
        }

        String prediction = response.get("prediction");

        // Step 4: Update the loan application with the prediction result
        loanApplication.setPredictionResult(prediction);
        loanApplicationRepository.save(loanApplication);

        return prediction;
    }
}
