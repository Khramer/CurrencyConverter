package com.example.CurrencyConverter.controller;

import com.example.CurrencyConverter.domain.MessageConverter;
import com.example.CurrencyConverter.repos.MessageRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.CurrencyConverter.myFunction.MyFunction.*;

//основной контроллер
@Controller
public class MainController {

    String finishSum;
    Map<String, Float> nameCurrencyAndValue;
    Date date;

    //путь к файлу XML
    @Value("${upload.path}")
    private String uploadPath;

    //путь к сайту с которого скачиваем файл XML
    @Value("${upload.pathCurrency}")
    private String uploadPathCurrency;

    //репозиторий всех запросов
    @Autowired
    private MessageRateRepo messageRateRepo;

    //начальная страница предлагающая перейти к конвертор
    @GetMapping("/")
    public String login() {
        return "main";
    }

    //вызов страницы с конвертер
    @GetMapping("/converterPage")
    public String my(Map<String, Object> model) throws IOException, SAXException, ParserConfigurationException {

        downloadFile(new File(uploadPath), uploadPathCurrency);
        nameCurrencyAndValue = (HashMap<String,Float>)parsXmlFile(new File(uploadPath));
        date = new  Date();

        model.put("amountOfCurrencyStart", "0");
        model.put("amountOfCurrencyFinish", "0");
        Iterable<MessageConverter> fullMessagesConverter = messageRateRepo.findAll();
        model.put("messages", fullMessagesConverter);

        return "converterPage";
    }

    //слушатель запросов на странице конвертер
    //принимающий число которое нужно конвертировать,первая валюта, вторая валюта
    @PostMapping("/converterPage")
    public String convector(
            @RequestParam String amountOfCurrency,
            @RequestParam String currencySelectOne,
            @RequestParam String currencySelectTwo,
            Map<String, Object> model
    ) {

        //если пришедшее сообщение вообще не имеет чисел, то заменяем ее на 0
        amountOfCurrency = amountOfCurrency.replaceAll("[^0-9]","");
        if(amountOfCurrency.equals(""))
            amountOfCurrency = "0";
        //считаем финальное значение через свою функцию functionCurrencyConverter
        finishSum = functionCurrencyConverter(amountOfCurrency, currencySelectOne, currencySelectTwo, nameCurrencyAndValue);

        //записываем новое сообщение
        //дата запроса
        //валюта из которой конвектируем
        //валюта в которую конвектируем
        //кол-во валюты
        //финальная сумма
        MessageConverter messageConverter  = new MessageConverter(date.toString(), currencySelectOne, currencySelectTwo, amountOfCurrency, finishSum);
        messageRateRepo.save(messageConverter);

        //записывем все в модель и отправляем на страницу
        model.put("amountOfCurrencyStart", amountOfCurrency);
        model.put("amountOfCurrencyFinish", finishSum);
        Iterable<MessageConverter> fullMessagesConverter = messageRateRepo.findAll();
        model.put("messages", fullMessagesConverter);

        return "converterPage";
    }
}

