package com.stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
//	public void insertTest(Connection conn) throws SQLException {
//		System.out.println("Database connected [" + conn.getMetaData().getURL());
//
//		GetMarketCap marketCapData = new GetMarketCap();
//		ArrayList<HashMap<String, String>> marketCapList = marketCapData.getStockData();
//
//		// 투자자별 매매동향 데이터 삽입을 제외할 종목
//		HashMap<String, String> exceptList = Const.getExceptCompany();
//
//		while (marketCapList.size() != 0) {
//			String company_code = marketCapList.get(0).get(Const.COL_NAME_COMPANY_CODE);
//			
//			if (!exceptList.containsKey(company_code)) {
//				insert(conn, company_code);
//			}
//			
//			marketCapList.remove(0);
//		}
//	}
    
	public void insert(Connection conn, String companyCode) {
		GetInvestorTrading investorTrading = new GetInvestorTrading();
      
		ArrayList<HashMap<String, String>> trandList = investorTrading.getStockData(companyCode);
        
		// 최근 삽입된 Trading 일자 가져오기
		String recentTradingDate = getRecentTradingDate(conn, companyCode);
		
		System.out.println("@@@ Company_code["+companyCode+"] Insert investor trading data Cnt: " + trandList.size() + ", recentTradingDate: " + recentTradingDate);

		PreparedStatement pstmt = null;
		
		while (trandList.size() != 0) {
			HashMap<String, String> trand = trandList.get(0);
			
            try {
	    		// 최근 삽입된 Trading 일자가 같으면 break
				if (recentTradingDate.equals(trand.get(Const.COL_NAME_TRADING_DATE))) {
					trandList.remove(trand);
		    		trand = null;
					break;
				}
				
	            // Trading 날짜가 없으면 Insert 하지 않음
	            Date tradingDate = stringToDate("yyyy.MM.dd", trand.get(Const.COL_NAME_TRADING_DATE));
	            if (tradingDate == null) {
	            	trandList.remove(trand);
	        		trand = null;
					continue;
				}
	            
				System.out.println("@@@ InsertInvestorTrading Insert data: " + trand.toString());
				
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

    		} catch (SQLException e) {
    			e.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			if(pstmt != null) try {pstmt.close();} catch (Exception e) { } 
    		}
    		
    		trandList.remove(trand);
    		trand = null;
		}
	}
	
	/**
	 * 투자자별 매매동향 최근 입력된 일자
	 * @param conn
	 * @param companyCode
	 * @return
	 */
	public String getRecentTradingDate(Connection conn, String companyCode) {
		System.out.println("@@@ Company_code["+companyCode+"] Insert investor trading data Cnt: ");
        
		String retVal = "";
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
            
        String sql = "";
        sql += "SELECT TO_CHAR(MAX(trading_date), 'yyyy.mm.dd') AS recent_trading_date ";
        sql += "  FROM investor_trading ";
        sql += " WHERE company_code = ? ";
       
        try {
            pstmt = conn.prepareStatement(sql);
            
            int idx = 1;
            setString(pstmt, idx++, companyCode);

            rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	retVal = rs.getString("recent_trading_date");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) { } 
			if(pstmt != null) try {pstmt.close();} catch (Exception e) { } 
		}
        
        return (retVal == null ? "" : retVal);
	}
}
