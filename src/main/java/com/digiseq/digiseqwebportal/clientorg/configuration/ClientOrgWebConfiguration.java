package com.digiseq.digiseqwebportal.clientorg.configuration;

import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.clientorg.controller.mapper.ClientOrgMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientOrgWebConfiguration {

  @Bean
  ClientOrgMapper clientOrgMapper() {
    return getMapper(ClientOrgMapper.class);
  }
}
