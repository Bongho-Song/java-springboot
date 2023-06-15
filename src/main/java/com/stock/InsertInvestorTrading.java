package com.stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.util.Const;
import com.util.SQLTransaction;

/**
 * 투자자별 매매동향 데이터를 PostgreSQL에 삽입하는 클래스
 * @author Bongho
 *
 */
@Component
public class InsertInvestorTrading extends SQLTransaction {

	// 테스트 소스
	public void insertTest(Connection conn) throws SQLException {
		System.out.println("Database connected [" + conn.getMetaData().getURL());

		GetMarketCap marketCapData = new GetMarketCap();
		ArrayList<HashMap<String, String>> marketCapList = marketCapData.getStockData();

		HashMap<String, String> exceptList = Const.getExceptCompany();
		System.out.println("size: " + exceptList.size());
		
		for (int i = 0; i < marketCapList.size(); i++) {
			String company_code = marketCapList.get(i).get(Const.COL_NAME_COMPANY_CODE);

			System.out.println("@@@ Company: " + company_code);
			
			if (!exceptList.containsKey(company_code)) {
				insert(conn, company_code);
			}
			
//			if (i == 2) {
//				break;
//			}
		}
	}
    
	public void insert(Connection conn, String companyCode) {
      GetInvestorTrading investorTrading = new GetInvestorTrading();
      ArrayList<HashMap<String, String>> trandList = investorTrading.getStockData(companyCode);
        
		PreparedStatement pstmt = null;
        
		try {
	    	for (int i = 0; i < trandList.size(); i++) {
	    		HashMap<String, String> trand = trandList.get(i);
	            
	    		// 테스트 소스
//	    		if (i == 5) {
//					break;
//				}
	    		// 테스트 소스
				if ("2023.06.15".equals(trand.get(Const.COL_NAME_TRADING_DATE))) {
					continue;
				}
				

	            // Trading 날짜가 없으면 Insert 하지 않음
	            Date tradingDate = stringToDate("yyyy.MM.dd", trand.get(Const.COL_NAME_TRADING_DATE));
	            if (tradingDate == null) {
					continue;
				}
	            
				System.out.println("@@@ InsertInvestorTrading Insert data["+i+"]: " + trand.toString());
				
	            String sql = "";
	            sql += "INSERT INTO investor_trading (";
	        	sql += " " + Const.COL_NAME_COMPANY_CODE;
	        	sql += "," + Const.COL_NAME_UPDOWN;
	        	sql += "," + Const.COL_NAME_TRADING_DATE;
	            sql += "," + Const.COL_NAME_PRICE_LAST;
	        	sql += "," + Const.COL_NAME_PRICE_DIFF;
	        	sql += "," + Const.COL_NAME_PRICE_RATE;
	        	sql += "," + Const.COL_NAME_TRADING_VOLUME;
	        	sql += "," + Const.COL_NAME_RETAIL_TRADING;
	        	sql += "," + Const.COL_NAME_FOREIGN_TRADING;
	        	sql += "," + Const.COL_NAME_FOREIGN_VOLUME;
	        	sql += "," + Const.COL_NAME_FOREIGN_VOLUME_RATE;
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
	            sql += ")";
	         	
	            pstmt = conn.prepareStatement(sql);
	            
	            int idx = 1;
	         	
	            setString(pstmt, idx++, trand.get(Const.COL_NAME_COMPANY_CODE));
	            setString(pstmt, idx++, trand.get(Const.COL_NAME_UPDOWN));
	            setDate(pstmt, idx++, new java.sql.Date(tradingDate.getTime()));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_PRICE_LAST));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_PRICE_DIFF));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_PRICE_RATE));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_TRADING_VOLUME));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_RETAIL_TRADING));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_FOREIGN_TRADING));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_FOREIGN_VOLUME));
	         	setDouble(pstmt, idx++, trand.get(Const.COL_NAME_FOREIGN_VOLUME_RATE));

	         	pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) try {pstmt.close();} catch (Exception e) { } 
		}
	}
}
