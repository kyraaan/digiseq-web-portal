package com.digiseq.digiseqwebportal.configuration.clientorg;

import com.digiseq.digiseqwebportal.service.ClientOrgService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientOrgServiceConfiguration {

  @Bean
  ClientOrgService clientOrgService() {
    return new ClientOrgService();
  }
}
