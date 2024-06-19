package org.henry.virtualaccountsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.henry.virtualaccountsystem.entity.User;


@Data
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountCreatedDTO {
    private String account_name;
    private String account_number;
    private String bank_code;
    private String bank_name;
    private String account_reference;
    private String unique_id;
    private String account_status;
    private String created_at;
    private String currency;
    private User customer;
}
