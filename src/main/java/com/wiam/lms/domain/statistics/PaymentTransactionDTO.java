package com.wiam.lms.domain.statistics;

import com.wiam.lms.domain.enumeration.PaymentSide;
import java.io.Serializable;

public class PaymentTransactionDTO implements Serializable {

    private PaymentSide side;
    private Long count;
    private Double totalAmount;
    private String currencyCode;

    public PaymentTransactionDTO(PaymentSide side, Long count, Double totalAmount, String currencyCode) {
        this.side = side;
        this.count = count;
        this.totalAmount = totalAmount;
        this.currencyCode = currencyCode;
    }

    // Getters and setters
    public PaymentSide getSide() {
        return side;
    }

    public void setSide(PaymentSide side) {
        this.side = side;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
