package com.digiseq.digiseqwebportal.clientorg.repository;

import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import java.util.List;
import java.util.Optional;

public interface ClientOrgRepository {

  List<ClientOrg> getClientOrgs();

  Optional<ClientOrg> getClientOrgById(Long clientOrgId);

  void deleteClientOrgById(Long clientOrgId);

  void saveClientOrg(ClientOrg clientOrg);

  void updateClientOrg(ClientOrg clientOrg);
}
