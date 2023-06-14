package com.javadeveloperzone;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.stock.StockData;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableScheduling
public class SpringBootApplication {
    public static void main(String[] args){
        SpringApplication.run(SpringBootApplication.class);
        
        StockData stockData = new StockData();
        ArrayList<HashMap<String, String>> stockList = stockData.getStockData();
        
        System.out.println("@@@@@@@@@@@@ " + stockList.size());
    }
}
