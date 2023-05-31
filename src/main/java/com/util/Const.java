package com.util;

public final class Const {
	public static final String URL_KOSPI_SEQUENCE = "https://finance.naver.com/sise/sise_market_sum.naver?&page="; // 시가총액 URL
	
	// 주식 관련 컬럼
	public static final String COL_NAME_SEQ = "SEQ"; // 시가총액 순서
	public static final String COL_NAME_COMPANY = "COMPANY";	// 종목명
	public static final String COL_NAME_PRICE_CURRENT = "PRICE_CURRENT";	// 현재가
	public static final String COL_NAME_PRICE_DIFF = "PRICE_DIFF";	// 전일비
	public static final String COL_NAME_PRICE_RATE = "PRICE_RATE";	// 등락률
	public static final String COL_NAME_PAR_VALUE = "PAR_VALUE";	// 액면가
	public static final String COL_NAME_PRICE_TOTAL = "PRICE_TOTAL";	// 시가총액
	public static final String COL_NAME_NUMBER_TOTAL = "NUMBER_TOTAL";	// 상장주식수
	public static final String COL_NAME_FOREIGNER_RATE = "FOREIGNER_RATE";	// 외국인비율
	public static final String COL_NAME_TRADING_VOLUME = "TRADING_VOLUME";	// 거래량
	public static final String COL_NAME_PER = "PER";	// PER
	public static final String COL_NAME_ROE = "ROE";	// ROE
	public static final String COL_NAME_UPDOWN = "UPDOWN";	// 상승/하락/보합
	public static final String COL_NAME_COMPANY_URL = "COMPANY_URL";	// 종목 상세 페이지
	public static final String COL_NAME_ENTER_DATE = "ENTER_DATE";	// 입력날짜
	
	// 컬럼 순서
	public static final String[] COL_TITLE_LIST = { 
				COL_NAME_SEQ
			, 	COL_NAME_COMPANY
			, 	COL_NAME_PRICE_CURRENT
			,	COL_NAME_PRICE_DIFF
			, 	COL_NAME_PRICE_RATE
			, 	COL_NAME_PAR_VALUE
			, 	COL_NAME_PRICE_TOTAL
			, 	COL_NAME_NUMBER_TOTAL
			, 	COL_NAME_FOREIGNER_RATE
			, 	COL_NAME_TRADING_VOLUME
			, 	COL_NAME_PER, COL_NAME_ROE };
	
	public static final String UPDOWN_UP = "UP";	// 상승
	public static final String UPDOWN_DOWN = "DOWN";	// 하락
	public static final String UPDOWN_NONE = "NONE";	// 보합
}
