package com.project.loan_management.services;


import com.banque.events.dto.MonthlyLoanPaying;
import com.project.loan_management.exceptions.TransactionServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionServiceImpl implements TransactionService{

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Value("${transaction.api.url}")
    private String transactionApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void sendPaymentRequest(MonthlyLoanPaying request) {
        try {
            restTemplate.postForObject(transactionApiUrl,request,String.class);
            logger.info("Send Payement request for account {}, whith amount :", request.getCompteId(),request.getAmount());
        } catch (HttpClientErrorException e) {
            logger.error("Error during the call to the transaction service, status {}, message : {}", e.getStatusCode(), e.getMessage());
            throw new TransactionServiceException("Error during the call to the transaction service : " +e.getMessage(),e);
        } catch (Exception e) {
            logger.error("Error during the call to the transaction service: {}", e.getMessage());
            throw new TransactionServiceException("Error during the call to the transaction service : "+e.getMessage(),e);
        }
    }
}
