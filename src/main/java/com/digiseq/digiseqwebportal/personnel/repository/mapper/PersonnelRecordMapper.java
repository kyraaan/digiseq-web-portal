package com.digiseq.digiseqwebportal.personnel.repository.mapper;

import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.repository.model.public_.tables.records.PersonnelRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonnelRecordMapper {

  @Mapping(source = "clientId", target = "clientOrgId")
  Personnel fromRecord(PersonnelRecord personnelRecord);
}
