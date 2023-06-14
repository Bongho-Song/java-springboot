package com.stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.util.Const;
import com.util.SQLTransaction;

public class InsertStockData extends SQLTransaction{

	public void insert(Connection conn) {
		StockData stockData = new StockData();
        ArrayList<HashMap<String, String>> stockList = stockData.getStockData();
        
		PreparedStatement pstmt = null;
        
		try {
			java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
			
	    	for (int i = 0; i < stockList.size(); i++) {
	    		HashMap<String, String> stock = stockList.get(i);
				System.out.println("["+i+"] Insert data: " + stock.toString());
	            
	            String sql = "";
	            sql += "INSERT INTO market_cap (";
	        	sql += " " + Const.COL_NAME_ENTER_DATE;
	        	sql += "," + Const.COL_NAME_COMPANY_CODE;
	        	sql += "," + Const.COL_NAME_COMPANY;
	            sql += "," + Const.COL_NAME_SEQ;
	        	sql += "," + Const.COL_NAME_PRICE_CURRENT;
	        	sql += "," + Const.COL_NAME_PRICE_DIFF;
	        	sql += "," + Const.COL_NAME_PRICE_RATE;
	        	sql += "," + Const.COL_NAME_PAR_VALUE;
	        	sql += "," + Const.COL_NAME_PRICE_TOTAL;
	        	sql += "," + Const.COL_NAME_NUMBER_TOTAL;
	        	sql += "," + Const.COL_NAME_FOREIGNER_RATE;
	        	sql += "," + Const.COL_NAME_TRADING_VOLUME;
	        	sql += "," + Const.COL_NAME_PER;
	        	sql += "," + Const.COL_NAME_ROE;
	        	sql += "," + Const.COL_NAME_UPDOWN;
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
	         	
	            pstmt = conn.prepareStatement(sql);
	            
	            int idx = 1;
	         	setDate(pstmt, idx++, sqlDate);
	            setString(pstmt, idx++, stock.get(Const.COL_NAME_COMPANY_CODE));
	            setString(pstmt, idx++, stock.get(Const.COL_NAME_COMPANY));
	            setInt(pstmt, idx++, stock.get(Const.COL_NAME_SEQ));
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

	         	pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) try {conn.close();} catch (Exception e) { } 
		}
	}
}
