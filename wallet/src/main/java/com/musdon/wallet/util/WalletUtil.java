package com.musdon.wallet.util;

import java.util.Random;

public class WalletUtil {
    public static String generateWallet(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<10; i++){
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateWallet());
    }

    public static final String DUPLICATE_WALLET_ERROR = "Wallet exists with this email";
    public static final String SUCCESS = "Success";
    public static final String ACCOUNT_CREATED = "Account has been successfully created";
    public static final String ERROR = "Error";
    public static final String ACCOUNT_NOT_FOUND = "Account number %s not found";
    public static final String DUPLICATE_TRANSACTION_ATTEMPT = "Duplicate Transaction Attempt";
    public static final String INSUFFICIENT_BALANCE = "Insufficient Balance";
    public static final String ACCOUNT_CREDITED = "Account has been successfully credited";
    public static final String ACCOUNT_DEBITED = "Account has been successfully debited";
    public static final String TRANSFER_SUCCESSFUL = "Transfer Completed";

}
