package com.digiseq.digiseqwebportal.service;

import static java.lang.String.format;

import com.digiseq.digiseqwebportal.controller.model.request.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdateClientOrgRequest;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientOrgService {
  private final ClientOrgRepository repository;

  public ClientOrgService(ClientOrgRepository repository) {
    this.repository = repository;
  }

  public List<ClientOrg> getClientOrgs() {
    return repository.getClientOrgs();
  }

  public ClientOrg getClientOrgById(Long clientOrgId) {
    try {
      return getClientOrg(clientOrgId);
    } catch (ClientOrgNotFoundException e) {
      log.error("No client org with id: {} was found", clientOrgId);
      throw e;
    }
  }

  public void saveClientOrg(AddClientOrgRequest request) {
    ClientOrg clientOrg =
        buildClientOrg(request.name(), request.registeredDate(), request.expiryDate());

    repository.saveClientOrg(clientOrg);
  }

  public void deleteClientOrgById(Long clientOrgId) {
    repository.deleteClientOrgById(clientOrgId);
  }

  public void updateClientOrg(Long clientOrgId, UpdateClientOrgRequest request) {
    ClientOrg clientOrg =
        buildClientOrg(request.name(), request.registeredDate(), request.expiryDate());
    repository.updateClientOrg(clientOrg);
  }

  private ClientOrg getClientOrg(Long clientOrgId) {
    return repository
        .getClientOrgById(clientOrgId)
        .orElseThrow(
            () ->
                new ClientOrgNotFoundException(
                    format("Client org with id: %s does not exist", clientOrgId)));
  }

  private static ClientOrg buildClientOrg(
      String name, LocalDate registeredDate, LocalDate expiryDate) {
    return ClientOrg.builder()
        .name(name)
        .registeredDate(registeredDate)
        .expiryDate(expiryDate)
        .build();
  }
}
