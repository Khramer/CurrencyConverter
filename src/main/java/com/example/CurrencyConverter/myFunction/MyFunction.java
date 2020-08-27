package com.example.CurrencyConverter.myFunction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyFunction {

    public static Map parsXmlFile(File file) throws ParserConfigurationException, IOException, SAXException {
        //Используем HashMap для хранения курса валют
        //ключом является наименование валюты, а хранимым значением является значение валюты
        //деленный на номинал
        Map<String, Float> nameCurrencyAndValue = new HashMap<String, Float>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nodeListCurrency = ((Element) nodeList.item(i)).getChildNodes();
            nameCurrencyAndValue.put(
                    nodeListCurrency.item(3).getTextContent(),
                    //сразу вычисляем истиное значение валюты разделив ее на номинал
                    Float.valueOf(nodeListCurrency.item(4).getTextContent().replace(",", "."))
                            /
                            Float.valueOf(nodeListCurrency.item(2).getTextContent())
            );
        }
        return nameCurrencyAndValue;
    }

    //конвертация происходит по формуле (кол-во_валюты*валюта_1\валюта_2)
    public static String functionCurrencyConverter(String valuteInputOne,
                                                   String startCurrency,
                                                   String finishCurrency,
                                                   Map<String, Float> hashMapCurrency){
        float value;
        float valueFinishCurrency = 0.0f;
        float valueStartCurrency = 0.0f;

        if (startCurrency.equals("Рубль"))
            valueStartCurrency = 1.0f;
        else
            valueStartCurrency = hashMapCurrency.get(startCurrency);

        if (finishCurrency.equals("Рубль"))
            valueFinishCurrency = 1.0f;
        else
            valueFinishCurrency = hashMapCurrency.get(finishCurrency);

        value = Float.parseFloat(valuteInputOne.replace(",", ".")) ;
        value = value * valueStartCurrency/valueFinishCurrency;

        return String.valueOf(value);
    }


    public static void downloadFile(File file, String uploadPathCurrency) throws IOException, ParserConfigurationException, SAXException {
        //если файла нет, то он скачивается, если он есть, но не за сегодня, то перезакачивается

        if(!file.exists()){
            URL url = new URL(uploadPathCurrency);
            InputStream inputStream = url.openStream();
            Files.copy(inputStream, file.toPath());
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        Element element = document.getDocumentElement();

        Date date = new  Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        if(!dateFormat.equals(element.getAttribute("Date"))){
            file.delete();
            URL url = new URL(uploadPathCurrency);
            InputStream inputStream = url.openStream();
            Files.copy(inputStream, file.toPath());
        }
    }
}
