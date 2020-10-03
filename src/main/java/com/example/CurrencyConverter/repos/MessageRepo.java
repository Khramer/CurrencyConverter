package com.example.CurrencyConverter.repos;


import com.example.CurrencyConverter.domain.MessageConverter;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<MessageConverter, Long> {
}
