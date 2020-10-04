package com.example.CurrencyConverter.controller;

import com.example.CurrencyConverter.domain.CurrencyValue;
import com.example.CurrencyConverter.domain.MessageConverter;
import com.example.CurrencyConverter.repos.CurrencyRepo;
import com.example.CurrencyConverter.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.CurrencyConverter.myFunction.MyFunction.*;

//основной контроллер
@EnableScheduling
@Controller
public class MainController {
    String resultSum;

    //путь к файлу XML в котором расположены курсы валют
    @Value("${upload.path}")
    private String uploadPath;

    //путь к сайту с которого скачиваем файл XML
    @Value("${upload.pathCurrency}")
    private String uploadPathCurrency;

    //репозиторий всех запросов
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private CurrencyRepo currencyRepo;

    @PostConstruct
    public void Setup() throws ParserConfigurationException, SAXException, IOException {
        downloadFile(new File(uploadPath), uploadPathCurrency);
        parsXmlFileInDateBase(new File(uploadPath), currencyRepo);
    }


    @Scheduled(cron = "* * 0 * * ?")
    public void reportCurrentTime() throws ParserConfigurationException, SAXException, IOException {
        downloadFile(new File(uploadPath), uploadPathCurrency);
        parsXmlFileInDateBase(new File(uploadPath), currencyRepo);
    }

    //начальная страница предлагающая перейти к конвертеру валют
    @GetMapping("/")
    public String rootPage() {
        return "main";
    }

    //вызов страницы с конвертером
    @GetMapping("/converterPage")
    public String converterPage(Map<String, Object> model) throws IOException, SAXException, ParserConfigurationException {
        downloadFile(new File(uploadPath), uploadPathCurrency);
        //namesСurrencieAndCoefficients = (HashMap<String,Float>)parsXmlFileInHashMap(new File(uploadPath));
        parsXmlFileInDateBase(new File(uploadPath), currencyRepo);

        model.put("nameCurrencyOne", "Рубли");
        model.put("nameCurrencyTwo", "Рубли");
        model.put("amountOfCurrencyInitial", "0");
        model.put("amountOfCurrencyResult", "0");

        return "converterPage";
    }

    //слушатель запросов на странице конвертер
    //принимающий число равное кол-ву валюты, которую будем конвертировать,
    //название валюты которую будем конвертировать, и название валюты в которую будем конвертировать
    @PostMapping("/converterPage")
    public String converter(
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
        //resultSum = String.valueOf(currencyConverter(Float.parseFloat(amountOfCurrency.replace(",", ".")), currencySelectOne, currencySelectTwo, namesСurrencieAndCoefficients));
        resultSum = String.valueOf(currencyConverterFromDB(Float.parseFloat(amountOfCurrency.replace(",", ".")), currencySelectOne, currencySelectTwo, currencyRepo));

        //записываем новое сообщение
        MessageConverter messageConverter  = new MessageConverter(LocalDateTime.now().toString(), currencySelectOne, currencySelectTwo, amountOfCurrency, resultSum);
        messageRepo.save(messageConverter);

        //записывем все в модель и отправляем на страницу
        model.put("nameCurrencyOne", currencySelectOne);
        model.put("nameCurrencyTwo", currencySelectTwo);
        model.put("amountOfCurrencyInitial", amountOfCurrency);
        model.put("amountOfCurrencyResult", resultSum);

        return "converterPage";
    }

    //вызов страницы с конвертером
    @GetMapping("/allRequest")
    public String allRequest(Map<String, Object> model) throws IOException, SAXException, ParserConfigurationException {
        Iterable<MessageConverter> allRequest = messageRepo.findAll();
        model.put("messages", allRequest);

        return "allRequest";
    }
}

