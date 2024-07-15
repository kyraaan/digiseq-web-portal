package com.digiseq.digiseqwebportal.clientorg.configuration;

import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.clientorg.repository.ClientOrgRepository;
import com.digiseq.digiseqwebportal.clientorg.repository.PostgresClientOrgRepository;
import com.digiseq.digiseqwebportal.clientorg.repository.mapper.ClientOrgRecordMapper;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientOrgRepositoryConfiguration {

  @Bean
  ClientOrgRepository clientOrgRepository(DSLContext dslContext, ClientOrgRecordMapper mapper) {
    return new PostgresClientOrgRepository(dslContext, mapper);
  }

  @Bean
  ClientOrgRecordMapper clientOrgRecordMapper() {
    return getMapper(ClientOrgRecordMapper.class);
  }
}
