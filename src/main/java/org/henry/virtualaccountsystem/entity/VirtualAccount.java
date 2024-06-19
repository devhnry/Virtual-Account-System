package org.henry.virtualaccountsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@Setter
@Getter
@ToString
@Table(name = "accounts")
public class VirtualAccount {
    @Setter
    @Getter
    @Id
    @SequenceGenerator(
            name = "accountSeq",
            sequenceName = "accountSeq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String account_name;
    private String account_number;
    private String bank_code;
    private String bank_name;
    private String account_reference;
    private String unique_id;
    private String account_status;
    private String created_at;
    private String currency;

//    @OneToOne
//    private User customer;
}
