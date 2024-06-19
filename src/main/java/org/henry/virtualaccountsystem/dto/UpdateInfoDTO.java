package org.henry.virtualaccountsystem.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateInfoDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long otpCode;
}
