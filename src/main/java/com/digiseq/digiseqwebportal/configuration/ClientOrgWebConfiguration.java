package com.digiseq.digiseqwebportal.configuration;

import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.controller.converter.ClientOrgMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientOrgWebConfiguration {

  @Bean
  ClientOrgMapper clientOrgMapper() {
    return getMapper(ClientOrgMapper.class);
  }
}
