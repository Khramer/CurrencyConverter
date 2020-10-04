package com.example.CurrencyConverter.myFunction;

import com.example.CurrencyConverter.domain.CurrencyValue;
import com.example.CurrencyConverter.repos.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.plaf.PanelUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class MyFunction {
    //конвертация происходит по формуле (кол-во_валюты*валюта_1\валюта_2)
    public static float currencyConverterFromDB(float initialSum,
                                          String nameInitialCurrency,
                                          String nameResultCurrency,
                                          CurrencyRepo currencyRepo){
        float resultSum;
        float amountInitialCurrency = 1.0f;
        float amountResultCurrency = 1.0f;

        if (!nameInitialCurrency.equals("Рубль"))
            amountResultCurrency = Float.valueOf(currencyRepo
                                                    .findByNameCurrency(nameInitialCurrency)
                                                    .get(0)
                                                    .getValueCurrency());
        if (!nameResultCurrency.equals("Рубль"))
            amountInitialCurrency = Float.valueOf(currencyRepo
                                                    .findByNameCurrency(nameResultCurrency)
                                                    .get(0)
                                                    .getValueCurrency());

        resultSum = initialSum * amountResultCurrency/amountInitialCurrency;

        return resultSum;
    }

    public static void downloadFile(File file, String uploadPathCurrency) throws IOException{
        if(file.exists())
            file.delete();
        URL url = new URL(uploadPathCurrency);
        InputStream inputStream = url.openStream();
        Files.copy(inputStream, file.toPath());
        inputStream.close();
    }

    public static void parsXmlFileInDateBase(File file, CurrencyRepo currencyRepo) throws ParserConfigurationException, IOException, SAXException {
        final int nameValuteMN = 3;
        final int valueValuteMN = 4;
        final int nominalValuteMN = 2;

        float resultValueCurrency = 0.0f;

        currencyRepo.deleteAll();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); ++i) {
            NodeList nodeListCurrency = ((Element) nodeList.item(i)).getChildNodes();
            //сразу вычисляем истиное значение валюты разделив ее на номинал
            resultValueCurrency = Float.valueOf(nodeListCurrency.item(valueValuteMN).getTextContent().replace(",", ".")) /
                    Float.valueOf(nodeListCurrency.item(nominalValuteMN).getTextContent());
            currencyRepo.save(new CurrencyValue(nodeListCurrency.item(nameValuteMN).getTextContent(),String.valueOf(resultValueCurrency)));
        }
    }
}
