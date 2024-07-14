package com.digiseq.digiseqwebportal.controller.converter;

import com.digiseq.digiseqwebportal.controller.model.response.ClientOrgResponse;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface ClientOrgMapper {
  List<ClientOrgResponse> toResponse(List<ClientOrg> clientOrgs);

  ClientOrgResponse toResponse(ClientOrg clientOrg);
}
