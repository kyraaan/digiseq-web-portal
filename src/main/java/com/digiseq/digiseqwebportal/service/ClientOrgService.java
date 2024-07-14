package com.digiseq.digiseqwebportal.service;

import static java.lang.String.format;

import com.digiseq.digiseqwebportal.controller.model.request.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdateClientOrgRequest;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.ClientOrgServiceException;
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
    try {
      return repository.getClientOrgs();
    } catch (Exception e) {
      log.error("Failed to retrieve client orgs: {} ", e.getMessage());
      throw new ClientOrgServiceException("Failed to retrieve client orgs", e);
    }
  }

  public ClientOrg getClientOrgById(Long clientOrgId) {
    try {
      return getClientOrg(clientOrgId);
    } catch (ClientOrgNotFoundException e) {
      log.error("No client org with id: {} was found", clientOrgId);
      throw e;
    } catch (Exception e) {
      throw new ClientOrgServiceException(
          format("Failed to retrieve client org with id: %s", clientOrgId), e);
    }
  }

  public void deleteClientOrgById(Long clientOrgId) {
    try {
      repository.deleteClientOrgById(clientOrgId);
    } catch (Exception e) {
      log.error(
          "Failed to delete client org with id: {} due to error: {}", clientOrgId, e.getMessage());
      throw new ClientOrgServiceException(
          format("Failed to delete client org with id: %s", clientOrgId), e);
    }
  }

  public void saveClientOrg(AddClientOrgRequest request) {
    ClientOrg clientOrg =
        buildClientOrg(request.name(), request.registeredDate(), request.expiryDate());
    try {
      repository.saveClientOrg(clientOrg);
    } catch (Exception e) {
      log.error("Failed to save client org due to error: {}", e.getMessage());
      throw new ClientOrgServiceException("Failed to save client org", e);
    }
  }

  public void updateClientOrg(Long clientOrgId, UpdateClientOrgRequest request) {
    ClientOrg clientOrg =
        buildClientOrg(request.name(), request.registeredDate(), request.expiryDate());
    try {
      repository.updateClientOrg(clientOrg);
    } catch (Exception e) {
      log.error(
          "Failed to update client org with id: {} due to error: {}", clientOrgId, e.getMessage());
      throw new ClientOrgServiceException("Failed to update client org", e);
    }
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
