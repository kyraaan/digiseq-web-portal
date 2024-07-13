package com.digiseq.digiseqwebportal.service;

import static java.lang.String.format;

import com.digiseq.digiseqwebportal.controller.model.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.controller.model.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.ClientOrgServiceException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.model.Personnel;
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
      log.error(
          "Failed to delete client org with id: {} due to error {}", clientOrgId, e.getMessage());
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

  public void saveClientOrg(AddClientOrgRequest request) {
    List<Personnel> personnel = buildPersonnel(request.personnel());
    ClientOrg clientOrg = buildClientOrg(request, personnel);
    try {
      repository.saveClientOrg(clientOrg);
    } catch (Exception e) {
      log.error("Failed to save client org due to error {}", e.getMessage());
      throw new ClientOrgServiceException("Failed to save client org", e);
    }
  }

  private static ClientOrg buildClientOrg(AddClientOrgRequest request, List<Personnel> personnel) {
    return ClientOrg.builder()
        .name(request.name())
        .registeredDate(request.registeredDate())
        .expiryDate(request.expiryDate())
        .personnel(personnel)
        .build();
  }

  private static List<Personnel> buildPersonnel(List<AddPersonnelRequest> personnelRequests) {
    return personnelRequests.stream()
        .map(
            personnel ->
                Personnel.builder()
                    .username(personnel.username())
                    .firstName(personnel.firstName())
                    .lastName(personnel.lastName())
                    .password(personnel.password()) // TODO password validation + status check
                    .email(personnel.email())
                    .phoneNumber(personnel.phoneNumber())
                    .build())
        .toList();
  }
}
