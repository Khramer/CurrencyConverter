package com.example.CurrencyConverter.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MessageConverter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String date;
    private String initialCurrency;
    private String resultCurrency;
    private String initialSum;
    private String resultSum;

    public MessageConverter(){}

    public MessageConverter(String date, String initialCurrency, String resultCurrency, String initialSum, String resultSum) {
        this.date = date;
        this.initialCurrency = initialCurrency;
        this.resultCurrency = resultCurrency;
        this.initialSum = initialSum;
        this.resultSum = resultSum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInitialCurrency() {
        return initialCurrency;
    }

    public void setInitialCurrency(String initialCurrency) {
        this.initialCurrency = initialCurrency;
    }

    public String getResultCurrency() {
        return resultCurrency;
    }

    public void setResultCurrency(String resultCurrency) {
        this.resultCurrency = resultCurrency;
    }

    public String getInitialSum() {
        return initialSum;
    }

    public void setInitialSum(String initialSum) {
        this.initialSum = initialSum;
    }

    public String getResultSum() {
        return resultSum;
    }

    public void setResultSum(String resultSum) {
        this.resultSum = resultSum;
    }
}
