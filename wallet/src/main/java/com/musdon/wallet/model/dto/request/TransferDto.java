package com.musdon.wallet.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDto {
    @NotNull(message = "account number cannot be null")
    @Size(min = 10, max = 10, message = "account number must be 10 digits")
    private String beneficiaryAccountNumber;
    @NotNull(message = "amount cannot be null")
    private Long amount;
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "sessionId must be a valid UUID"
    )
    private String sessionId;
    @NotNull(message = "account number cannot be null")
    @Size(min = 10, max = 10, message = "account number must be 10 digits")
    private String sourceAccountNumber;
}
