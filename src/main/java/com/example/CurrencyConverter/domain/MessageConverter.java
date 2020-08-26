package com.example.CurrencyConverter.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MessageConverter {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String date;
    private String startCurrency;
    private String finishCurrency;
    private String startSum;
    private String finishSum;

    public MessageConverter(){}

    public MessageConverter(String date, String startCurrency, String finishCurrency, String startSum, String finishSum) {
        this.date = date;
        this.startCurrency = startCurrency;
        this.finishCurrency = finishCurrency;
        this.startSum = startSum;
        this.finishSum = finishSum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartCurrency() {
        return startCurrency;
    }

    public void setStartCurrency(String startCurrency) {
        this.startCurrency = startCurrency;
    }

    public String getFinishCurrency() {
        return finishCurrency;
    }

    public void setFinishCurrency(String finishCurrency) {
        this.finishCurrency = finishCurrency;
    }

    public String getStartSum() {
        return startSum;
    }

    public void setStartSum(String startSum) {
        this.startSum = startSum;
    }

    public String getFinishSum() {
        return finishSum;
    }

    public void setFinishSum(String finishSum) {
        this.finishSum = finishSum;
    }
}
