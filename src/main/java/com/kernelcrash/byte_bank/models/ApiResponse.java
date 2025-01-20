package com.kernelcrash.byte_bank.models;

import java.math.BigDecimal;
import java.util.List;

public class ApiResponse {
    private String Response;
    private String Message;
    private boolean HasWarning;
    private int Type;
    private RateLimit RateLimit;
    public static Data Data;
}


class RateLimit {
}