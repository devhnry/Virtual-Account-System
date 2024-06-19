package org.henry.virtualaccountsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.henry.virtualaccountsystem.dto.enums.TokenType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private Boolean expired;
    private Boolean revoked;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private Customer users;

    @Override
    public String toString(){
        return "Token: " + token;
    }

}
