package com.digiseq.digiseqwebportal.personnel.configuration;

import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.personnel.repository.PersonnelRepository;
import com.digiseq.digiseqwebportal.personnel.repository.PostgresPersonnelRepository;
import com.digiseq.digiseqwebportal.personnel.repository.mapper.PersonnelRecordMapper;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonnelRepositoryConfiguration {

  @Bean
  PersonnelRepository personnelRepository(DSLContext dsl, PersonnelRecordMapper mapper) {
    return new PostgresPersonnelRepository(dsl, mapper);
  }

  @Bean
  PersonnelRecordMapper personnelRecordMapper() {
    return getMapper(PersonnelRecordMapper.class);
  }
}
