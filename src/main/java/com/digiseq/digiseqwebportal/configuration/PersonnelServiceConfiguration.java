package com.digiseq.digiseqwebportal.configuration;

import com.digiseq.digiseqwebportal.repository.PersonnelRepository;
import com.digiseq.digiseqwebportal.service.PersonnelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonnelServiceConfiguration {

  @Bean
  PersonnelService personnelService(PersonnelRepository personnelRepository) {
    return new PersonnelService(personnelRepository);
  }
}
