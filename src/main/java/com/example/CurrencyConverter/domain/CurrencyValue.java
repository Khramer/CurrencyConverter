package com.example.CurrencyConverter.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CurrencyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nameCurrency;
    private String valueCurrency;

    public CurrencyValue(){}

    public CurrencyValue(String nameCurrency, String valueCurrency){
        this.nameCurrency = nameCurrency;
        this.valueCurrency = valueCurrency;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public String getValueCurrency() {
        return valueCurrency;
    }

    public void setValueCurrency(String valueCurrency) {
        this.valueCurrency = valueCurrency;
    }
}
