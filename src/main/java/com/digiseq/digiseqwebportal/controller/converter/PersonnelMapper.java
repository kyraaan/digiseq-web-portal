package com.digiseq.digiseqwebportal.controller.converter;

import com.digiseq.digiseqwebportal.controller.model.response.PersonnelResponse;
import com.digiseq.digiseqwebportal.model.Personnel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface PersonnelMapper {
  List<PersonnelResponse> toResponse(List<Personnel> personnel);

  PersonnelResponse toResponse(Personnel personnel);
}
