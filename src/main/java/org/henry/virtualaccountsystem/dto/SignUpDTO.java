package org.henry.virtualaccountsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class SignUpDTO {
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
}
