package com.javadeveloperzone;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.InsertInvestorTrading;
import com.stock.InsertMarketCap;
import com.util.Const;

@Component
public class Scheduler {
	
	@Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

//	"0 0 * * * *" = the top of every hour of every day.
//	"*/10 * * * * *" = 매 10초마다 실행한다.
//	"0 0 8-10 * * *" = 매일 8, 9, 10시에 실행한다
//	"0 0 6,19 * * *" = 매일 오전 6시, 오후 7시에 실행한다.
//	"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
//	"0 0 9-17 * * MON-FRI" = 오전 9시부터 오후 5시까지 주중(월~금)에 실행한다.
//	"0 0 0 25 12 ?" = every Christmas Day at midnight
//    @Scheduled(cron = "0 0/1 * * * MON-FRI")
	@Scheduled(cron = "0 0 18 * * MON-FRI")
    public void printDate () {
		System.out.println("@@@ Schedule Start !!! " + getCurrentDate());
		
        Connection conn = null;
        
        try {
        	conn = dataSource.getConnection();
        	System.out.println("Database connected ["+dataSource.getClass()+"] ["+conn.getMetaData().getURL());
            
            // 시가총액 Insert
            InsertMarketCap insertStockData = new InsertMarketCap();
            ArrayList<HashMap<String, String>> marketCapList = insertStockData.insert(conn);
            
            // 투자자별 매매동향
            if (marketCapList != null) {
//        		// 투자자별 매매동향 제외 대상 목록
        		HashMap<String, String> exceptList = Const.getExceptCompany();

        		InsertInvestorTrading insertInvestorTrading = new InsertInvestorTrading(); 
            	for (int i = 0; i < marketCapList.size(); i++) {
            		String company_code = marketCapList.get(0).get(Const.COL_NAME_COMPANY_CODE);
        			
        			if (!exceptList.containsKey(company_code)) {
        				insertInvestorTrading.insert(conn, company_code);
        			}
            	}
			}
            
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) { 
	    		try {conn.close();} catch (Exception e) { } 
	    		System.out.println("Connection Close Complete !!! ");
	    	} 
		}
    	
    	System.out.println("@@@ Schedule End !!! " + getCurrentDate());
    }
    
    private String getCurrentDate() {
    	SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }
}
