package com.digiseq.digiseqwebportal.configuration.clientorg;

import com.digiseq.digiseqwebportal.service.PersonnelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonnelServiceConfiguration {

  @Bean
  PersonnelService personnelService() {
    return new PersonnelService(null);
  }
}
