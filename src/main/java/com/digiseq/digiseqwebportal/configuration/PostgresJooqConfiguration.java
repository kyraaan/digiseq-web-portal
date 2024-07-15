package com.digiseq.digiseqwebportal.configuration;

import static org.jooq.SQLDialect.POSTGRES;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresJooqConfiguration {

  @Bean
  DSLContext dsl(DataSource dataSource) {
    return DSL.using(dataSource, POSTGRES);
  }
}
