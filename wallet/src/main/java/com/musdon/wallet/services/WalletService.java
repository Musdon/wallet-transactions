package com.musdon.wallet.services;

import com.musdon.wallet.model.dto.request.CreateWalletDto;
import com.musdon.wallet.model.dto.response.ApiResponse;
import com.musdon.wallet.model.dto.response.WalletResponse;
import com.musdon.wallet.model.entity.Wallet;
import com.musdon.wallet.repository.TransactionRepository;
import com.musdon.wallet.repository.WalletRepository;
import com.musdon.wallet.util.WalletUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

import static com.musdon.wallet.util.WalletUtil.*;


@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public WalletResponse createWallet(CreateWalletDto createWalletDto){
        Optional<Wallet> existingWallet = walletRepository.findByEmail(createWalletDto.getEmail());
        if (existingWallet.isPresent()){
            throw new EntityExistsException(DUPLICATE_WALLET_ERROR);
        }
        Wallet wallet = Wallet.builder()
                .firstName(createWalletDto.getFirstName())
                .lastName(createWalletDto.getLastName())
                .email(createWalletDto.getEmail())
                .accountBalance(0L)
                .accountNumber(generateWallet())
                .build();
        wallet = walletRepository.save(wallet);
        return modelMapper.map(wallet, WalletResponse.class);
    }

    public WalletResponse fetchWalletDetails(Long id){
        Optional<Wallet> existingWallet = walletRepository.findById(id);
        if (existingWallet.isEmpty()){
            throw new EntityNotFoundException(ACCOUNT_NOT_FOUND);
        }
        Wallet wallet = existingWallet.get();
        return modelMapper.map(wallet, WalletResponse.class);
    }
}
