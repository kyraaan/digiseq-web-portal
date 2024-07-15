package com.digiseq.digiseqwebportal.configuration;

import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import com.digiseq.digiseqwebportal.service.ClientOrgService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientOrgServiceConfiguration {

  @Bean
  ClientOrgService clientOrgService(ClientOrgRepository clientOrgRepository) {
    return new ClientOrgService(clientOrgRepository);
  }
}
