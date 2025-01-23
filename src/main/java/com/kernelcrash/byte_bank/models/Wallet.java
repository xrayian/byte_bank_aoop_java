package com.kernelcrash.byte_bank.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

public class Wallet implements Serializable {
    private Long walletId;
    private String walletName;
    private String cryptoType;
    private double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean primary;
    private List<Transaction> transactions;
    private User user;

    public Wallet(Long walletId, String walletName, String cryptoType, double balance, LocalDateTime createdAt, LocalDateTime updatedAt, boolean primary, List<Transaction> transactions, User user) {
        this.walletId = walletId;
        this.walletName = walletName;
        this.cryptoType = cryptoType;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.primary = primary;
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
        return balance;
    }

    public double getUIFriendlyBalance() {
        return new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP).doubleValue();
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

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isPrimary() {
        return primary;
    }
}