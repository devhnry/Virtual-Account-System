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
@Table(name = "cards")
public class VirtualCard {
    @Setter
    @Getter
    @Id
    @SequenceGenerator(
            name = "cardSeq",
            sequenceName = "cardSeq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
}
