package com.util;

public final class Const {
	public static final String URL_KOSPI_SEQUENCE = "https://finance.naver.com/sise/sise_market_sum.naver?&page="; // 시가총액 URL
	
	// 주식 관련 컬럼
	public static final String COL_NAME_SEQ = "seq"; // 시가총액 순서
	public static final String COL_NAME_COMPANY = "company";	// 종목명
	public static final String COL_NAME_PRICE_CURRENT = "price_current";	// 현재가
	public static final String COL_NAME_PRICE_DIFF = "price_diff";	// 전일비
	public static final String COL_NAME_PRICE_RATE = "price_rate";	// 등락률
	public static final String COL_NAME_PAR_VALUE = "par_value";	// 액면가
	public static final String COL_NAME_PRICE_TOTAL = "price_total";	// 시가총액
	public static final String COL_NAME_NUMBER_TOTAL = "number_total";	// 상장주식수
	public static final String COL_NAME_FOREIGNER_RATE = "foreigner_rate";	// 외국인비율
	public static final String COL_NAME_TRADING_VOLUME = "trading_volume";	// 거래량
	public static final String COL_NAME_PER = "per";	// PER
	public static final String COL_NAME_ROE = "roe";	// ROE
	public static final String COL_NAME_UPDOWN = "updown";	// 상승/하락/보합
	public static final String COL_NAME_COMPANY_URL = "company_url";	// 종목 상세 페이지
	public static final String COL_NAME_ENTER_DATE = "enter_date";	// 입력날짜
	
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
