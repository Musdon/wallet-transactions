package com.musdon.wallet.services;

import com.musdon.wallet.model.dto.request.CreditDebitWalletDto;
import com.musdon.wallet.model.dto.request.TransferDto;
import com.musdon.wallet.model.dto.response.Credit;
import com.musdon.wallet.model.dto.response.Debit;
import com.musdon.wallet.model.dto.response.WalletResponse;
import com.musdon.wallet.model.entity.Transaction;
import com.musdon.wallet.model.entity.Wallet;
import com.musdon.wallet.model.enums.TransactionType;
import com.musdon.wallet.repository.TransactionRepository;
import com.musdon.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.musdon.wallet.util.WalletUtil.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public WalletResponse creditWallet(CreditDebitWalletDto creditDebitWalletDto){
        Optional<Wallet> wallet = walletRepository.findByAccountNumber(creditDebitWalletDto.getAccountNumber());
        Optional<Transaction> transaction = transactionRepository.findByTransactionReference(creditDebitWalletDto.getSessionId());
        Wallet walletToCredit;
        if (wallet.isEmpty()){
            throw new EntityNotFoundException(ACCOUNT_NOT_FOUND);
        } else {
            walletToCredit = wallet.get();
        }
        if (transaction.isPresent() && !transaction.get().getTransactionType().equals(TransactionType.TRANSFER)){
            throw new DuplicateKeyException(DUPLICATE_TRANSACTION_ATTEMPT);
        }

        walletToCredit.setAccountBalance(walletToCredit.getAccountBalance() + creditDebitWalletDto.getAmount());
        walletToCredit = walletRepository.save(walletToCredit);
        Transaction creditTransaction = Transaction.builder()
                .amount(creditDebitWalletDto.getAmount())
                .transactionReference(creditDebitWalletDto.getSessionId())
                .wallet(walletToCredit)
                .transactionType(TransactionType.CREDIT)
                .build();
        transactionRepository.save(creditTransaction);
        WalletResponse walletResponse = modelMapper.map(walletToCredit, WalletResponse.class);
        walletResponse.setTransactionReference(creditTransaction.getTransactionReference());
        return walletResponse;
    }

    public WalletResponse debitWallet(CreditDebitWalletDto creditDebitWalletDto){
        Optional<Wallet> existingWallet = walletRepository.findByAccountNumber(creditDebitWalletDto.getAccountNumber());
        Wallet wallet;

        if (existingWallet.isEmpty()){
            throw new EntityNotFoundException(ACCOUNT_NOT_FOUND);
        }
        wallet = existingWallet.get();
        if (wallet.getAccountBalance() < creditDebitWalletDto.getAmount()){
            throw new IllegalArgumentException(INSUFFICIENT_BALANCE);
        }
        wallet.setAccountBalance(wallet.getAccountBalance() - creditDebitWalletDto.getAmount());
        walletRepository.save(wallet);
        Transaction debitTransaction = Transaction.builder()
                .amount(creditDebitWalletDto.getAmount())
                .transactionReference(creditDebitWalletDto.getSessionId())
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .build();
        transactionRepository.save(debitTransaction);
        WalletResponse walletResponse = modelMapper.map(wallet, WalletResponse.class);
        walletResponse.setTransactionReference(debitTransaction.getTransactionReference());
        return walletResponse;
    }

    @Transactional
    public WalletResponse transferFunds(TransferDto transferDto){
        Optional<Wallet> checkSourceAccount = walletRepository.findByAccountNumber(transferDto.getSourceAccountNumber());
        Optional<Wallet> checkBeneficiaryAccount = walletRepository.findByAccountNumber(transferDto.getBeneficiaryAccountNumber());
        if (checkSourceAccount.isEmpty() || checkBeneficiaryAccount.isEmpty()){
            throw new EntityNotFoundException(ACCOUNT_NOT_FOUND);
        }
        Wallet sourceAccount = checkSourceAccount.get();
        Wallet beneficiaryAccount = checkBeneficiaryAccount.get();
        if (sourceAccount.getAccountBalance() < transferDto.getAmount()){
            throw new IllegalArgumentException(INSUFFICIENT_BALANCE);
        }
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance() - transferDto.getAmount());
        beneficiaryAccount.setAccountBalance(beneficiaryAccount.getAccountBalance() + transferDto.getAmount());
        sourceAccount = walletRepository.save(sourceAccount);
        beneficiaryAccount = walletRepository.save(beneficiaryAccount);
        Transaction creditTransaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .wallet(beneficiaryAccount)
                .transactionReference(transferDto.getSessionId())
                .amount(transferDto.getAmount())
                .build();
        Transaction debitTransaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .wallet(sourceAccount)
                .transactionReference(transferDto.getSessionId())
                .amount(transferDto.getAmount())
                .build();
        transactionRepository.save(creditTransaction);
        transactionRepository.save(debitTransaction);

        WalletResponse walletResponse = WalletResponse.builder()
                .creditDetails(Credit.builder()
                        .accountBalance(String.valueOf(beneficiaryAccount.getAccountBalance()))
                        .accountNumber(beneficiaryAccount.getAccountNumber())
                        .build())
                .debitDetails(Debit.builder()
                        .accountBalance(String.valueOf(sourceAccount.getAccountBalance()))
                        .accountNumber(sourceAccount.getAccountNumber())
                        .build())
                .build();
        walletResponse.setTransactionReference(transferDto.getSessionId());
        return walletResponse;
    }
}
