package com.musdon.wallet.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long accountBalance;
    private String accountNumber;
    private String createdAt;
    private String updatedAt;
    private String transactionReference;
    private Credit creditDetails;
    private Debit debitDetails;
}
