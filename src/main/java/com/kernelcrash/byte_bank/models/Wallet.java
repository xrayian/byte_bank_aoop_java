package com.kernelcrash.byte_bank.models;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.CryptoDataFetcher;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        if (Objects.equals(walletName, ""))
            walletName = "Unnamed Wallet";
        return cryptoType + " (" + walletName + ")";
    }

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

    public double getConversionRate(Wallet receiveWallet) {
        HashMap<String, CurrencyUSDValue> latestPrices = CurrencyDataStore.getLatestCurrencyPriceList();

        if (latestPrices.isEmpty()) {
            throw new RuntimeException("Currency prices not loaded yet");
        }

        CurrencyUSDValue payCurrency = latestPrices.get(cryptoType);
        CurrencyUSDValue receiveCurrency = latestPrices.get(receiveWallet.getCryptoType());
        double rateInverse = receiveCurrency.getUnitCurrencyValueInUSD().divide(payCurrency.getUnitCurrencyValueInUSD(), 10, RoundingMode.HALF_UP).doubleValue();
        return rateInverse == 0 ? 0 : 1 / rateInverse;
    }

    public String getWalletId() {
        return walletId.toString();
    }
}