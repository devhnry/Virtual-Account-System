package org.henry.virtualaccountsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponseDTO {
    private Boolean status;
    private String message;
    private TransactionDTO data;
}
