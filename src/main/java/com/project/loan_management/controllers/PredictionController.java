package com.project.loan_management.controllers;

import com.project.loan_management.services.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/{id}")
    public String predictLoanApproval(@PathVariable Long id) {
        return predictionService.getPredictionAndUpdateResult(id);
    }
}
