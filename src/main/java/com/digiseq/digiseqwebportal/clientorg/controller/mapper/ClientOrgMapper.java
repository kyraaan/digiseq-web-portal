package com.digiseq.digiseqwebportal.clientorg.controller.mapper;

import com.digiseq.digiseqwebportal.clientorg.controller.model.response.ClientOrgResponse;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface ClientOrgMapper {
  List<ClientOrgResponse> toResponse(List<ClientOrg> clientOrgs);

  ClientOrgResponse toResponse(ClientOrg clientOrg);
}
