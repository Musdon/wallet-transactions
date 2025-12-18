package com.musdon.wallet.controller;

import com.musdon.wallet.model.dto.request.CreateWalletDto;
import com.musdon.wallet.model.dto.response.ApiResponse;
import com.musdon.wallet.model.dto.response.WalletResponse;
import com.musdon.wallet.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.musdon.wallet.util.WalletUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wallet")
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<ApiResponse<WalletResponse>> createWallet(@Valid @RequestBody CreateWalletDto createWalletDto){
        try {
            WalletResponse walletResponse = walletService.createWallet(createWalletDto);
            return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                            .status(SUCCESS)
                            .message(ACCOUNT_CREATED)
                            .data(walletResponse)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<WalletResponse>> fetchWallet(@PathVariable Long id){
        try {
            WalletResponse walletResponse = walletService.fetchWalletDetails(id);
            return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                            .status(SUCCESS)
                            .message(SUCCESS)
                            .data(walletResponse)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), ACCOUNT_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage(), String.valueOf(e.getStackTrace()[1])));
        }
    }
}
