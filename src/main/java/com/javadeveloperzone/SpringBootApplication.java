package com.javadeveloperzone;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableScheduling
public class SpringBootApplication {
    
    public static void main(String[] args){
        SpringApplication.run(SpringBootApplication.class);
        
        // 테스트: 시가총액 목록
//        StockData stockData = new StockData();
//        ArrayList<HashMap<String, String>> stockList = stockData.getStockData();
//        System.out.println("@@@@@@@@@@@@ " + stockList.size());
        
        // 테스트: 투자자별 매매동향
//        GetInvestorTrading stockTrend = new GetInvestorTrading();
//        ArrayList<HashMap<String, String>> stockTrendList = stockTrend.getStockData("352820");
//        System.out.println("@@@@@@@@@@@@ " + stockTrendList.size());
        
//        InsertInvestorTrading iit = new InsertInvestorTrading();
//        iit.insertTest();
    }
}
