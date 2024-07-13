package com.digiseq.digiseqwebportal.service;

import static java.lang.String.format;

import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.ClientOrgServiceException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
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
      throw new ClientOrgServiceException(
          format("Failed to delete client org with id: %s", clientOrgId), e);
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
}
