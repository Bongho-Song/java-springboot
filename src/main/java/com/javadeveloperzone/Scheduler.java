package com.javadeveloperzone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.StockData;
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
	@Scheduled(cron = "0 0 18 * * MON-FRI")
    public void printDate () throws SQLException {
        System.out.println(new Date().toString());
        StockData stockData = new StockData();
        ArrayList<HashMap<String, String>> stockList = stockData.getStockData();
        
        Connection conn = dataSource.getConnection();
        System.out.println(dataSource.getClass());
        System.out.println(conn.getMetaData().getURL());
        System.out.println(conn.getMetaData().getUserName());
        
        System.out.println("Database connected ["+dataSource.getClass()+"] ["+conn.getMetaData().getURL());
        
        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    	for (int i = 0; i < stockList.size(); i++) {
    		HashMap<String, String> stock = stockList.get(i);
			System.out.println("["+i+"] data: " + stock.toString());
            
            String sql = "";
            sql += "INSERT INTO \"STOCK_SEQ_SUM\" (";
            sql += " \"" + Const.COL_NAME_SEQ + "\"";
        	sql += ",\"" + Const.COL_NAME_COMPANY + "\"";
        	sql += ",\"" + Const.COL_NAME_PRICE_CURRENT + "\"";
        	sql += ",\"" + Const.COL_NAME_PRICE_DIFF + "\"";
        	sql += ",\"" + Const.COL_NAME_PRICE_RATE + "\"";
        	sql += ",\"" + Const.COL_NAME_PAR_VALUE + "\"";
        	sql += ",\"" + Const.COL_NAME_PRICE_TOTAL + "\"";
        	sql += ",\"" + Const.COL_NAME_NUMBER_TOTAL + "\"";
        	sql += ",\"" + Const.COL_NAME_FOREIGNER_RATE + "\"";
        	sql += ",\"" + Const.COL_NAME_TRADING_VOLUME + "\"";
        	sql += ",\"" + Const.COL_NAME_PER + "\"";
        	sql += ",\"" + Const.COL_NAME_ROE + "\"";
        	sql += ",\"" + Const.COL_NAME_UPDOWN + "\"";
        	sql += ",\"" + Const.COL_NAME_COMPANY_URL + "\"";
        	sql += ",\"" + Const.COL_NAME_ENTER_DATE+ "\"";
            sql += ") VALUES (";
            sql += " ?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
        	sql += ",?";
            sql += ")";
         	
            System.out.println(sql);
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            int idx = 1;
            setInt(pstmt, idx++, stock.get(Const.COL_NAME_SEQ));
            setString(pstmt, idx++, stock.get(Const.COL_NAME_COMPANY));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_PRICE_CURRENT));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_PRICE_DIFF));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_PRICE_RATE));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_PAR_VALUE));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_PRICE_TOTAL));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_NUMBER_TOTAL));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_FOREIGNER_RATE));
         	setInt(pstmt, idx++, stock.get(Const.COL_NAME_TRADING_VOLUME));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_PER));
         	setDouble(pstmt, idx++, stock.get(Const.COL_NAME_ROE));
         	setString(pstmt, idx++, stock.get(Const.COL_NAME_UPDOWN));
         	setString(pstmt, idx++, stock.get(Const.COL_NAME_COMPANY_URL));
         	setDate(pstmt, idx++, sqlDate);

         	pstmt.executeUpdate();
            
            System.out.println("["+i+"] Insert Complete !!! ");
		}
    	
    	if(conn != null) {
    		conn.close();
    		System.out.println("Connection Close Complete !!! ");
    	} 
    	
    }
	
	private void setDate(PreparedStatement pstmt, int idx, java.sql.Date v) throws SQLException {
		pstmt.setDate(idx++, v);
	}
	
	private void setString(PreparedStatement pstmt, int idx, String v) throws SQLException {
		pstmt.setString(idx++, v);
	}
	
	private void setInt(PreparedStatement pstmt, int idx, String v) throws SQLException {
		if ("N/A".equals(v) || v == null) {
			pstmt.setNull(idx, Types.INTEGER);
		} else {
			pstmt.setDouble(idx, Integer.parseInt(v));
		}
	}
	
	private void setDouble(PreparedStatement pstmt, int idx, String v) throws SQLException {
		if ("N/A".equals(v) || v == null) {
			pstmt.setNull(idx, Types.DOUBLE);
		} else {
			pstmt.setDouble(idx, Double.parseDouble(v));
		}
	}
}
