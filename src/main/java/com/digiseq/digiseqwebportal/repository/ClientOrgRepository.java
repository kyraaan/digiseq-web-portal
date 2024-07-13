package com.digiseq.digiseqwebportal.repository;

import com.digiseq.digiseqwebportal.model.ClientOrg;
import java.util.List;
import java.util.Optional;

public interface ClientOrgRepository {

  List<ClientOrg> getClientOrgs();

  Optional<ClientOrg> getClientOrgById(Long clientOrgId);
}
