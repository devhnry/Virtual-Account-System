package org.henry.virtualaccountsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.henry.virtualaccountsystem.entity.KYC;
import org.henry.virtualaccountsystem.entity.User;

@Data
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRequestDTO {
    private String account_name;
    private String account_reference;
    private Boolean permanent;
    private String bank_code;
    private User customer;
    private KYC kyc;
}
