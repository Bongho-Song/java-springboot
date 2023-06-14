package com.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class SQLTransaction {

	protected void setDate(PreparedStatement pstmt, int idx, java.sql.Date v) throws SQLException {
		pstmt.setDate(idx++, v);
	}
	
	protected void setString(PreparedStatement pstmt, int idx, String v) throws SQLException {
		pstmt.setString(idx++, v);
	}
	
	protected void setInt(PreparedStatement pstmt, int idx, String v) throws SQLException {
		if ("N/A".equals(v) || v == null) {
			pstmt.setNull(idx, Types.INTEGER);
		} else {
			pstmt.setDouble(idx, Integer.parseInt(v));
		}
	}
	
	protected void setDouble(PreparedStatement pstmt, int idx, String v) throws SQLException {
		if ("N/A".equals(v) || v == null) {
			pstmt.setNull(idx, Types.DOUBLE);
		} else {
			pstmt.setDouble(idx, Double.parseDouble(v));
		}
	}
}
