package com.digiseq.digiseqwebportal.configuration;

import com.digiseq.digiseqwebportal.repository.PersonnelRepository;
import com.digiseq.digiseqwebportal.service.PersonnelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PersonnelServiceConfiguration {

  @Bean
  PersonnelService personnelService(
      PersonnelRepository personnelRepository, PasswordEncoder passwordEncoder) {
    return new PersonnelService(personnelRepository, passwordEncoder);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
