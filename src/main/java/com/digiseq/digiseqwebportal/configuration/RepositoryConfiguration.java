package com.digiseq.digiseqwebportal.configuration;

import static org.jooq.SQLDialect.POSTGRES;
import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import com.digiseq.digiseqwebportal.repository.PersonnelRepository;
import com.digiseq.digiseqwebportal.repository.PostgresClientOrgRepository;
import com.digiseq.digiseqwebportal.repository.PostgresPersonnelRepository;
import com.digiseq.digiseqwebportal.repository.mapper.ClientOrgRecordMapper;
import com.digiseq.digiseqwebportal.repository.mapper.PersonnelRecordMapper;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

  @Bean
  DSLContext dsl(DataSource dataSource) {
    return DSL.using(dataSource, POSTGRES);
  }

  @Bean
  PersonnelRepository personnelRepository(DSLContext dsl, PersonnelRecordMapper mapper) {
    return new PostgresPersonnelRepository(dsl, mapper);
  }

  @Bean
  PersonnelRecordMapper personnelRecordMapper() {
    return getMapper(PersonnelRecordMapper.class);
  }

  @Bean
  ClientOrgRepository clientOrgRepository(DSLContext dslContext, ClientOrgRecordMapper mapper) {
    return new PostgresClientOrgRepository(dslContext, mapper);
  }

  @Bean
  ClientOrgRecordMapper clientOrgRecordMapper() {
    return getMapper(ClientOrgRecordMapper.class);
  }
}
