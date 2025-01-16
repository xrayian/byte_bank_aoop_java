package com.kernelcrash.byte_bank.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class User implements Serializable {
    private String userId;
    private String username;
    private String email;
    private String passwordHash;
    private double accountBalance;
    private String role;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Wallet> wallets;

    public User(
            String userId, String username, String email, String passwordHash, double accountBalance, List<Wallet> wallets, String role, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.accountBalance = accountBalance;
        this.wallets = wallets;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addWallet(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public boolean isActive() {
        return isActive;
    }
}
