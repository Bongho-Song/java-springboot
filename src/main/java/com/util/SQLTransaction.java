package com.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLTransaction {
	
	/**
	 * PreparedStatement에 Date 파라미터 입력
	 * @param pstmt
	 * @param idx
	 * @param v
	 * @throws SQLException
	 */
	protected void setDate(PreparedStatement pstmt, int idx, java.sql.Date v) throws SQLException {
		pstmt.setDate(idx++, v);
	}
	
	/**
	 * PreparedStatement에 String 파라미터 입력
	 * @param pstmt
	 * @param idx
	 * @param v
	 * @throws SQLException
	 */
	protected void setString(PreparedStatement pstmt, int idx, String v) throws SQLException {
		pstmt.setString(idx++, v);
	}
	
	/**
	 * PreparedStatement에 Integer 파라미터 입력
	 * @param pstmt
	 * @param idx
	 * @param v
	 * @throws SQLException
	 */
	protected void setInt(PreparedStatement pstmt, int idx, String v) throws SQLException {
		if ("N/A".equals(v) || v == null) {
			pstmt.setNull(idx, Types.INTEGER);
		} else {
			pstmt.setDouble(idx, Integer.parseInt(v));
		}
	}

	/**
	 * PreparedStatement에 Double 파라미터 입력
	 * @param pstmt
	 * @param idx
	 * @param v
	 * @throws SQLException
	 */
	protected void setDouble(PreparedStatement pstmt, int idx, String v) throws SQLException {
		if ("N/A".equals(v) || v == null) {
			pstmt.setNull(idx, Types.DOUBLE);
		} else {
			pstmt.setDouble(idx, Double.parseDouble(v));
		}
	}
	
	/**
	 * String 형태의 날짜를 Date 형태로 변환
	 * @param pattern
	 * @param strDate
	 * @return
	 */
	protected Date stringToDate(String pattern, String strDate) {
		Date retDate = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		try {
			retDate = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return retDate;
	}
}
