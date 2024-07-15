package com.digiseq.digiseqwebportal.configuration;

import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.controller.converter.PersonnelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonnelWebConfiguration {

  @Bean
  PersonnelMapper personnelMapper() {
    return getMapper(PersonnelMapper.class);
  }
}
