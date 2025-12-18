package com.musdon.wallet.controller;

import com.musdon.wallet.model.dto.request.CreditDebitWalletDto;
import com.musdon.wallet.model.dto.request.TransferDto;
import com.musdon.wallet.model.dto.response.ApiResponse;
import com.musdon.wallet.model.dto.response.WalletResponse;
import com.musdon.wallet.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.musdon.wallet.util.WalletUtil.*;

@RestController
@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("credit")
    public ResponseEntity<ApiResponse<WalletResponse>> creditAccount(@Valid @RequestBody CreditDebitWalletDto creditDebitWalletDto){
        try {
            WalletResponse walletResponse = transactionService.creditWallet(creditDebitWalletDto);
            return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                            .status(SUCCESS)
                            .message(ACCOUNT_CREDITED)
                            .data(walletResponse)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        }
    }

    @PostMapping("debit")
    public ResponseEntity<ApiResponse<WalletResponse>> debitAccount(@Valid @RequestBody CreditDebitWalletDto creditDebitWalletDto){
        try {
            WalletResponse walletResponse = transactionService.debitWallet(creditDebitWalletDto);
            return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                            .status(SUCCESS)
                            .message(ACCOUNT_DEBITED)
                            .data(walletResponse)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        }
    }

    @PostMapping("transfer")
    public ResponseEntity<ApiResponse<WalletResponse>> transferFunds(@Valid @RequestBody TransferDto transferDto){
        try{
            WalletResponse walletResponse = transactionService.transferFunds(transferDto);
            return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                            .status(SUCCESS)
                            .message(TRANSFER_SUCCESSFUL)
                            .data(walletResponse)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        }
    }
}
