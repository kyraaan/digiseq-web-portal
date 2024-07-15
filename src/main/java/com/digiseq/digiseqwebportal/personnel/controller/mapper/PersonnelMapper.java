package com.digiseq.digiseqwebportal.personnel.controller.mapper;

import com.digiseq.digiseqwebportal.personnel.controller.model.response.PersonnelResponse;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface PersonnelMapper {
  List<PersonnelResponse> toResponse(List<Personnel> personnel);

  PersonnelResponse toResponse(Personnel personnel);
}
