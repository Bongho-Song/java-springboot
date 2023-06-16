package com.javadeveloperzone;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.stock.InsertInvestorTrading;

@Component
public class PostgreSQLConfig implements ApplicationRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public PostgreSQLConfig(DataSource dataSource,
                            JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        try (Connection connection = dataSource.getConnection()){
//            Statement statement = connection.createStatement();
//            String sql = "CREATE TABLE TBL_TEST(NO INTEGER NOT NULL, TEST_NAME VARCHAR(255), PRIMARY KEY (NO))";
//            statement.executeUpdate(sql);
//        }

//        jdbcTemplate.execute("INSERT INTO TBL_TEST VALUES (1, 'ssjeong')");
    	
    	// 테스트 소스
//    	Connection conn = null;
//    	try {
//        	conn = dataSource.getConnection();
//            
//        	// 시가총액 Insert
//        	InsertInvestorTrading iit = new InsertInvestorTrading();
//        	iit.insertTest(conn);
//        	
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(conn != null) { 
//	    		try {conn.close();} catch (Exception e) { } 
//	    		System.out.println("Connection Close Complete !!! ");
//	    	} 
//		}
    }
}