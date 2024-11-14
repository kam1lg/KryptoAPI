package com.example;

import com.example.service.KryptoService;
import com.example.service.DbService;

import org.knowm.xchange.dto.marketdata.Ticker;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class KryptoAPI {
    public static void main(String[] args) {

        KryptoService serwisKrypto = new KryptoService(); //połączenie do bitstampa i pobieranie
        DbService serwisBaza = new DbService(); //połączenie z bazą i inserty

        //pary walutowe krypto/docelowa
        List<String> currencies = Arrays.asList(
                "BTC/USD",
                "ETH/USD",
                "LTC/USD",
                "XRP/USD",
                "BCH/USD");

        //zmienne przechowujące pobrane kursy
        Double btcValue = null;
        Double ethValue = null;
        Double ltcValue = null;
        Double xrpValue = null;
        Double bchValue = null;

        //miejsce do zapisu csv - folder roboczy
        String currentDir = System.getProperty("user.dir");
        String filePath = Paths.get(currentDir, "kursy.csv").toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (PrintWriter writer = new PrintWriter(filePath)) {
            //pętla iteruje po każdej walucie z osobna
            for (String currency : currencies) {
                String[] pair = currency.split("/"); //rozdzielenie pary walutowej
                String baseCurrency = pair[0];
                String quoteCurrency = pair[1];
                //pobranie kursu
                try {
                    Ticker ticker = serwisKrypto.getData(baseCurrency, quoteCurrency);
                    String message = sdf.format(new Date()) + "," + baseCurrency + "/" + quoteCurrency + "," + ticker.getLast();
                    writer.println(message);//zapis do csv
                    //w zależności od waluty w pętli przypisywana jest wartość do zmiennej
                    switch (baseCurrency) {
                        case "BTC": btcValue = ticker.getLast().doubleValue(); break;
                        case "ETH": ethValue = ticker.getLast().doubleValue(); break;
                        case "LTC": ltcValue = ticker.getLast().doubleValue(); break;
                        case "XRP": xrpValue = ticker.getLast().doubleValue(); break;
                        case "BCH": bchValue = ticker.getLast().doubleValue(); break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //insert do bazy
            try {
                serwisBaza.insertData(sdf.format(new Date()), btcValue, ethValue, ltcValue, xrpValue, bchValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
