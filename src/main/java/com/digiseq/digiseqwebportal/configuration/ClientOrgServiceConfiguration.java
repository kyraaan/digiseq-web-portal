package com.digiseq.digiseqwebportal.configuration;

import static com.digiseq.digiseqwebportal.configuration.ClientOrgServiceConfiguration.*;

import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import com.digiseq.digiseqwebportal.service.ClientOrgService;
import com.digiseq.digiseqwebportal.service.ClientOrgStatusCalculator;
import com.digiseq.digiseqwebportal.service.DateProvider;
import java.time.Clock;
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

  @Bean
  DateProvider dateProvider(Clock clock) {
    return new DateProvider(clock);
  }

  @Bean
  Clock clock() {
    return Clock.systemDefaultZone();
  }

  @ConfigurationProperties(prefix = "client-org")
  @Value
  static class StatusProperties {
    int daysUntilMarkedExpiringSoon;
  }
}
