package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Account number cannot be blank")
    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @NotBlank(message = "Holder name cannot be blank")
    @Column(nullable = false)
    private String holderName;

   @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance cannot be negative")
    @Column(nullable = false)
    private Double balance;
}