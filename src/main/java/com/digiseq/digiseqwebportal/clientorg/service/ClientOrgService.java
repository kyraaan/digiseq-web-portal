package com.digiseq.digiseqwebportal.clientorg.service;

import static java.lang.String.format;

import com.digiseq.digiseqwebportal.clientorg.controller.model.request.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.clientorg.controller.model.request.UpdateClientOrgRequest;
import com.digiseq.digiseqwebportal.clientorg.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus;
import com.digiseq.digiseqwebportal.clientorg.repository.ClientOrgRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientOrgService {
  private final ClientOrgRepository repository;
  private final ClientOrgStatusCalculator calculator;

  public ClientOrgService(ClientOrgRepository repository, ClientOrgStatusCalculator calculator) {
    this.repository = repository;
    this.calculator = calculator;
  }

  public List<ClientOrg> getClientOrgs() {
    List<ClientOrg> clientOrgs = repository.getClientOrgs();
    return clientOrgs.stream().map(this::withUpdatedStatus).toList();
  }

  public ClientOrg getClientOrgById(Long clientOrgId) {
    try {
      ClientOrg clientOrg = getClientOrg(clientOrgId);
      return withUpdatedStatus(clientOrg);
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

  private ClientOrg withUpdatedStatus(ClientOrg clientOrg) {
    ClientOrgStatus status = calculator.calculateStatus(clientOrg.expiryDate());
    return clientOrg.toBuilder().status(status).build();
  }

  private static ClientOrg buildClientOrg(
      String name, LocalDate registeredDate, LocalDate expiryDate) {
    return ClientOrg.builder()
        .name(name)
        .registeredDate(registeredDate)
        .expiryDate(expiryDate)
        .status(null)
        .build();
  }
}
