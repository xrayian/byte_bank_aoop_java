package com.kernelcrash.byte_bank.models;

import java.io.Serializable;

public class Transaction implements Serializable {
    private Long transactionId;         
    private String type;                
    private double amount;              
    private String description;         
    private String timestamp;           
    private Wallet wallet;
}
