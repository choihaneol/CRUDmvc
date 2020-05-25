package org.zerock.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages="org.zerock.service") //=root-context.xml
@MapperScan(basePackages= {"org.zerock.mapper"})
public class RootConfig {

  @Bean
  public DataSource dataSource() { //Hikari:DB끌어오기 위한 라이브러리 
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy"); //드라이버 
    hikariConfig.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@localhost:1521:XE"); //서버 

    hikariConfig.setUsername("book_ex"); //서버 계정 로그인  
    hikariConfig.setPassword("book_ex");

    HikariDataSource dataSource = new HikariDataSource(hikariConfig); //dataSource에 Hikari로 끌어온 DB저

    return dataSource;
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {  //dataSource에 대한 예외처리 
    SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
    sqlSessionFactory.setDataSource(dataSource()); 
    return (SqlSessionFactory) sqlSessionFactory.getObject();
  }

}
