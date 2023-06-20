package com.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import com.util.Const;
import com.util.StringUtil;

public class GetInvestorTrading {
	
	public ArrayList<HashMap<String, String>> getStockData(String company_code) {
		ArrayList<HashMap<String, String>> stockList = new ArrayList<>();
		
		int page = 1;
		int maxPage = 20;
		
		String prev_trading_date = "";
		while (true) {
			ArrayList<HashMap<String, String>> retList = getStockTradingTrend(company_code, page);
			
			// 반환된 데이터가 없거나 MaxPage가 넘어가면 break
			if (retList.size() == 0 || page > maxPage) {
				break;
			}
			
			// 이전 날짜와 조회된 List의 0번째의 날짜가 같다면 break -> 최근 상장된 주식은 67번째 Page가 없음에되 데이터가 조회됨
			String trading_date = retList.get(0).get(Const.COL_NAME_TRADING_DATE);
			if (prev_trading_date.equals(trading_date)) {
				break;
			}
			
			prev_trading_date = trading_date;
			
			stockList.addAll(retList);
			
			page++;
		}
		
		return stockList;
	}
	private ArrayList<HashMap<String, String>> getStockTradingTrend(String company_code, int page) {
		ArrayList<HashMap<String, String>> retList = new ArrayList<>();
		
		// 시가 총액 화면
		String urlString = Const.URL_ITEM_TRADING_TREND + page;
		urlString = urlString.replace("{code}", company_code);
		
		String line = null;
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader reader = null; 
		HttpsURLConnection httpsConn = null;
		
		try {
			URL url = new URL(urlString);
			httpsConn = (HttpsURLConnection) url.openConnection();
			
			httpsConn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			
			httpsConn.setDoInput(true);
			httpsConn.setUseCaches(false);
			httpsConn.setReadTimeout(1000);
			httpsConn.setConnectTimeout(1000);
			httpsConn.setRequestMethod("GET");
			httpsConn.setRequestProperty("HeaderKey","HeaderValue");
			httpsConn.setRequestProperty("User-agent","Mozilla/5.0");

			int responseCode = httpsConn.getResponseCode();
//			System.out.println("RespCode [" + responseCode + "] URL: " + urlString);
			
			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, null);
			httpsConn.setSSLSocketFactory(context.getSocketFactory());
			
			httpsConn.connect();
			httpsConn.setInstanceFollowRedirects(true);
			
			if (responseCode == HttpsURLConnection.HTTP_OK) { // 정상 호출 200
				in = httpsConn.getInputStream();
			} else {
				in = httpsConn.getErrorStream();
			}
			
			isr = new InputStreamReader(in, "euc-kr");
			reader = new BufferedReader(isr);

			boolean kospi_table_start = false;
			
			String fullHtml = "";
			
			while ((line = reader.readLine()) != null) {
				//테이블 시작
				if (line.contains("summary=\"외국인 기관 순매매 거래량")) {
					kospi_table_start = true;
					continue;
				} 
				
				if (kospi_table_start) {
					if (line != null) {
						line = line.replace("\n\r", "").replace("\t", "");
					}
					
					fullHtml += line;
					
					// 테이블 종료
					if (line.contains("</table>")) {
						break;	
					}	
				}
			}
			
			// Full HTML 출력
//			System.out.println(fullHtml);
			
			ArrayList<String> trList = new ArrayList<String>();
			
			int indexStart = 0;
			int indexEnd = 0;
			
			while (true) {
				String findStrStart = "<tr";
				String findStrEnd = "</tr>";
			
				indexStart = fullHtml.indexOf(findStrStart, indexEnd);
				indexEnd = fullHtml.indexOf(findStrEnd, indexEnd) + findStrEnd.length();
				
				// 끝까지 다 돌음
				if (indexStart < 0 || indexEnd < 0) {
					break;
				}

				String trStr = fullHtml.substring(indexStart, indexEnd);
				
				// 필요한 문자열만 추출
				if (trStr.contains("<th") || trStr.contains("onMouseOver")) {
//					System.out.println(" @@@ TEST 출력 TR : " + "[" + indexStart + " ~ " + indexEnd + "] " + trStr);
					trList.add(trStr);
				}
			}
			
			// Header 찾기 (Naver 화면이 변경되었을때 확인용)
//			String titleStr = vtHtml.get(0);
//			String title = "";
//			indexStart = 0;
//			indexEnd = 0;
//			
//			while (true) {
//				String findStrStart = "<th scope=\"col\"";
//				String findStrEnd = "</th>";
//			
//				indexStart = titleStr.indexOf(">", findStrStart.length() + indexEnd) + 1;
//				indexEnd = titleStr.indexOf(findStrEnd, indexEnd);
//				
//				// 끝까지 다 돌음
//				if (indexStart < 0 || indexEnd < 0) {
//					break;
//				}
//				
//				String tdStr = titleStr.substring(indexStart, indexEnd);
//				
//				if (!"".equals(title)) {
//					title += "/";
//				}
//				title += tdStr;
//				
//				indexEnd += findStrEnd.length();
//			}
//			System.out.println(title);
			
			HashMap<String, String> stockMap = null;
			
			/**
			 * Header Title 순서
			 * -> N/종목명/현재가/전일비/등락률/액면가/시가총액/상장주식수/외국인비율/거래량/PER/ROE/토론실
			 * 
			 * trList의 셈플 데이터
			 * [0]: <tr><th scope="col">N</th><th scope="col">종목명</th><th scope="col">현재가</th><th scope="col" class="tr" style="padding-right:8px">전일비</th><th scope="col">등락률</th><th scope="col">액면가</th><th scope="col">시가총액</th><th scope="col">상장주식수</th><th scope="col">외국인비율</th><th scope="col">거래량</th><th scope="col">PER</th><th scope="col">ROE</th><th scope="col">토론실</th></tr>
			 * [1]: <tr  onMouseOver="mouseOver(this)" onMouseOut="mouseOut(this)"><td class="no">1</td><td><a href="/item/main.naver?code=005930" class="tltle">삼성전자</a></td><td class="number">60,600</td><td class="number"><img src="https://ssl.pstatic.net/imgstock/images/images4/ico_up.gif" width="7" height="6" style="margin-right:4px;" alt="상승"><span class="tah p11 red02">100</span></td><td class="number"><span class="tah p11 red01">+0.17%</span></td><td class="number">100</td><td class="number">3,617,688</td><td class="number">5,969,783</td><td class="number">50.69</td><td class="number">11,768,887</td><td class="number">7.52</td><td class="number">17.07</td><td class="center"><a href="/item/board.naver?code=005930"><img src="https://ssl.pstatic.net/imgstock/images5/ico_debatebl2.gif" width="15" height="13" alt="토론실"></a></td></tr>
			 * [2]: <tr  onMouseOver="mouseOver(this)" onMouseOut="mouseOut(this)"><td class="no">2</td><td><a href="/item/main.naver?code=373220" class="tltle">LG에너지솔루션</a></td><td class="number">523,000</td><td class="number"><img src="https://ssl.pstatic.net/imgstock/images/images4/ico_up.gif" width="7" height="6" style="margin-right:4px;" alt="상승"><span class="tah p11 red02">15,000</span></td><td class="number"><span class="tah p11 red01">+2.95%</span></td><td class="number">500</td><td class="number">1,223,820</td><td class="number">234,000</td><td class="number">5.27</td><td class="number">413,128</td><td class="number">213.12</td><td class="number">10.68</td><td class="center"><a href="/item/board.naver?code=373220"><img src="https://ssl.pstatic.net/imgstock/images5/ico_debatebl2.gif" width="15" height="13" alt="토론실"></a></td></tr>
			 */
			// Body (주식종목) 찾기
			for (int i = 2; i < trList.size(); i++) {
//			for (int i = 2; i < 3; i++) {
				String bodyStr = trList.get(i);
				indexStart = 0;
				indexEnd = 0;

				String updown = "";
				String detailUrl = "";
				
				stockMap = new HashMap<>();
				int colIdx = 0;
				
				// TD 시작과 끝을 찾아 순차적으로 필요한 데이터를 추출함
				while (true) {
					String findStrStart = "<td";
					String findStrEnd = "</td>";
				
					indexStart = bodyStr.indexOf(findStrStart, indexEnd);
					indexEnd = bodyStr.indexOf(findStrEnd, indexEnd) + findStrEnd.length();;
					
					// 끝까지 다 돌음
					if (indexStart < 0 || indexEnd < 0) {
						break;
					}
					
					// TD 태그의 시작과 종료까지의 문자열을 가져옴
					String tdStr = bodyStr.substring(indexStart, indexEnd);
//					System.out.println(" @@@ TEST 출력 주식종목 : " + tdStr);
					
					if (tdStr.contains("&nbsp;")) {
						continue;
					}
					
					/**
					 * 종목 상세 페이지 url
					 * 셈플 데이터: <td><a href="/item/main.naver?code=005930" class="tltle">삼성전자</a></td>
					 */
					if (tdStr.contains("class=\"tltle\"") && StringUtil.isNull(detailUrl)) {
						String hrefStr = "href=\"";
						int tmpIdx = tdStr.indexOf(hrefStr) + hrefStr.length();
						detailUrl = tdStr.substring(tmpIdx, tdStr.indexOf("\"", tmpIdx));
					}
					
					// 값만 가져오기 위한 문자열 자르기
					findStrStart = ">";
					findStrEnd = "<";
					
					int subIndexStart = 0;
					int subIndexEnd = 0;
					
					String tagSpan = "<span";
					String tagA = "<a";
					if (tdStr.contains(tagSpan)) {
						subIndexStart = tdStr.indexOf(tagSpan) + tagSpan.length();
						subIndexEnd = subIndexStart;
						
					} else if (tdStr.contains(tagA)) {
						subIndexStart = tdStr.indexOf(tagA) + tagA.length();
						subIndexEnd = subIndexStart;
					} else {
						subIndexEnd = tdStr.indexOf(">");
					}
					
					// html 태그에서 값만 가져옴
					String value = tdStr.substring(tdStr.indexOf(findStrStart, subIndexStart) + 1, tdStr.indexOf(findStrEnd, subIndexEnd));
					
					// 상승/하락/보합 확인
					if ("".equals(updown) && value.contains("%")) {
						updown = StringUtil.getUpDown(value);	
					}
					
					// 문자열 치환(",", "%", "+", "-" 문자열은 사용하지 않음)
					value = StringUtil.deleteSpecialSymbol(value);
					
					stockMap.put(Const.TREND_COL_TITLE_LIST[colIdx], value);
					
					colIdx++;
				}
				
				if (stockMap.size() != 0) {
					stockMap.put(Const.COL_NAME_COMPANY_CODE, company_code);
					stockMap.put(Const.COL_NAME_UPDOWN, updown);
					
					retList.add(stockMap);
				}
				
				stockMap = null;
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("UnknownHostException : " + e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println(urlString + " is not a URL I understand");
        } catch (IOException e) {
        	e.printStackTrace();
        	System.out.println("IOException :" + e);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("error : " + e);
        } finally {
        	if (in != null) try {in.close();} catch (Exception e) {}
        	if (isr != null) try {isr.close();} catch (Exception e) {}
            if (reader != null) try {reader.close();} catch (Exception e) {}
            if (httpsConn != null) httpsConn.disconnect(); 
        }	
		
		return retList;
	}
}
