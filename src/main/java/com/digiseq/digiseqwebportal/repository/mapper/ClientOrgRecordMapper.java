package com.digiseq.digiseqwebportal.repository.mapper;

import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.repository.model.public_.tables.records.ClientorgRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientOrgRecordMapper {

  @Mapping(source = "clientId", target = "clientOrgId")
  ClientOrg fromRecord(ClientorgRecord clientorgRecord);
}
