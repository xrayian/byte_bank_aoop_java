package com.kernelcrash.byte_bank.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Wallet implements Serializable {
    private Long walletId;
    private String walletName;
    private String cryptoType;
    private double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPrimary;
    private List<Transaction> transactions;
    private User user;

    public Wallet(Long walletId, String walletName, String cryptoType, double balance, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isPrimary, List<Transaction> transactions, User user) {
        this.walletId = walletId;
        this.walletName = walletName;
        this.cryptoType = cryptoType;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPrimary = isPrimary;
        this.transactions = transactions;
        this.user = user;
    }

    public Wallet() {

    }

    public String getWalletName() {
        return walletName;
    }

    public String getCryptoType() {
        return cryptoType;
    }

    public double getBalance() {
//        //calculate the balance from the transactions
//        getTransactions().forEach(transaction -> {
//            if (transaction.getType().contains("DEPOSIT") || transaction.getType().contains("TRANSFER") || transaction.getType().contains("Bonus") || transaction.getType().contains("Interest") || transaction.getType().contains("Cashback")) {
//                balance += transaction.getAmount();
//            } else if (transaction.getType().equals("WITHDRAWAL")) {
//                balance -= transaction.getAmount();
//            }
//        });
        return balance;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public User getUser() {
        return user;
    }
}