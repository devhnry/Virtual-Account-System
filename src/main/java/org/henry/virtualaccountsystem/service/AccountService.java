package org.henry.virtualaccountsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.henry.virtualaccountsystem.dto.*;
import org.henry.virtualaccountsystem.service.utils.PasswordValidation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final PasswordValidation passwordValidation;

    private final RestTemplate restTemplate;
    private static String secretKey = "sk_test_cNNwP1m6g3iDBFDjVDFL7xLFTsgL3WZ2HCsn3diK";

    private String baseUrl = "https://api.korapay.com/merchant/api/v1";
    private static String VBA = "/virtual-bank-account";
    private static String VBATransaction = "/virtual-bank-account/transactions";


    private static HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + secretKey);
        return headers;
    }

    public KoraResponse createVirtualAccount(AccountRequestDTO request) {
        KoraResponse res = new KoraResponse();
        try {
            request.setPermanent(true);
            request.setBank_code("000");
            request.setAccount_reference(passwordValidation.generateReference());
            HttpEntity<AccountRequestDTO> httpEntity = new HttpEntity<>(request, getHttpHeaders());
            String url = baseUrl + VBA;
            var response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, KoraResponse.class);
            return response.getBody();
        }catch (Exception e){
            res.setStatus(false);
            res.setMessage(e.getMessage());
            return res;
        }
    }

    public KoraResponse getVirtualAccountDetails(AccountRequestDTO requestDTO){
        KoraResponse res = new KoraResponse();
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<>(getHttpHeaders());
            String url = baseUrl + VBA + "/" + requestDTO.getAccount_reference();
            System.out.println(url);
            var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, KoraResponse.class);
            return response.getBody();
        }catch (Exception e){
            res.setStatus(false);
            res.setMessage(e.getMessage());
            return res;
        }
    }

    public KoraResponse creditVirtualResponse(FundAccountDTO fund){
        KoraResponse res = new KoraResponse();
        try{
            fund.setCurrency("USD");
            HttpEntity<FundAccountDTO> httpEntity = new HttpEntity<>(fund, getHttpHeaders());
            String url = baseUrl + VBA + "/sandbox/credit";
            System.out.println(url);
            var response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, KoraResponse.class);
            return response.getBody();
        }catch (Exception e){
            res.setStatus(false);
            res.setMessage(e.getMessage());
            return res;
        }
    }

    public TransactionResponseDTO viewTransactions(FundAccountDTO fund){
        TransactionResponseDTO res = new TransactionResponseDTO();
        try{
            HttpEntity<FundAccountDTO> httpEntity = new HttpEntity<>(fund, getHttpHeaders());
            String url = baseUrl + VBATransaction + "?account_number=" + fund.getAccount_number();
            log.info(url);
            var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, TransactionResponseDTO.class);
            log.info("Response body gotten from Kora");
            return response.getBody();
        }catch (Exception e){
            log.error("Error calling Api");
            res.setStatus(false);
            res.setMessage(e.getMessage());
            return res;
        }
    }

//    public
}
