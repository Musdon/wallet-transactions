package com.musdon.wallet.repository;

import com.musdon.wallet.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByEmail(String email);
    Optional<Wallet> findByAccountNumber(String accountNumber);
}
