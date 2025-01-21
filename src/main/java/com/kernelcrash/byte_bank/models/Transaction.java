package com.kernelcrash.byte_bank.models;

import java.io.Serializable;
import java.time.Instant;

public class Transaction implements Serializable {
    private Long transactionId;         
    private String type;                
    private double amount;              
    private String description;         
    private String timestamp;
    private Wallet wallet;

    public Long getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getType() {
        return type;
    }

}
