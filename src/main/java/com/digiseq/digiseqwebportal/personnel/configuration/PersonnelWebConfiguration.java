package com.digiseq.digiseqwebportal.personnel.configuration;

import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.personnel.controller.mapper.PersonnelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonnelWebConfiguration {

  @Bean
  PersonnelMapper personnelMapper() {
    return getMapper(PersonnelMapper.class);
  }
}
