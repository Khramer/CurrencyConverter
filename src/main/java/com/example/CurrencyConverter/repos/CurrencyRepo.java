package com.example.CurrencyConverter.repos;

import com.example.CurrencyConverter.domain.CurrencyValue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepo extends CrudRepository<CurrencyValue, Long> {
    List<CurrencyValue> findByNameCurrency(String nameCurrency);
}
