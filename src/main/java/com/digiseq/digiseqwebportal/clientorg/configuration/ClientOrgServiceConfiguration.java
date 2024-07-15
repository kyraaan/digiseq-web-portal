package com.digiseq.digiseqwebportal.clientorg.configuration;

import static com.digiseq.digiseqwebportal.clientorg.configuration.ClientOrgServiceConfiguration.StatusProperties;

import com.digiseq.digiseqwebportal.clientorg.repository.ClientOrgRepository;
import com.digiseq.digiseqwebportal.clientorg.service.ClientOrgService;
import com.digiseq.digiseqwebportal.clientorg.service.ClientOrgStatusCalculator;
import com.digiseq.digiseqwebportal.util.DateProvider;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StatusProperties.class)
public class ClientOrgServiceConfiguration {
  private final StatusProperties properties;

  ClientOrgServiceConfiguration(StatusProperties properties) {
    this.properties = properties;
  }

  @Bean
  ClientOrgService clientOrgService(
      ClientOrgRepository clientOrgRepository, ClientOrgStatusCalculator statusCalculator) {
    return new ClientOrgService(clientOrgRepository, statusCalculator);
  }

  @Bean
  ClientOrgStatusCalculator clientOrgStatusCalculator(DateProvider dateProvider) {
    return new ClientOrgStatusCalculator(dateProvider, properties.getDaysUntilMarkedExpiringSoon());
  }

  @ConfigurationProperties(prefix = "client-org")
  @Value
  static class StatusProperties {
    int daysUntilMarkedExpiringSoon;
  }
}
