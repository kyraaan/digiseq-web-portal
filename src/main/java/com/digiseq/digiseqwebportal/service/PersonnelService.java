package com.digiseq.digiseqwebportal.service;

import static java.lang.String.format;

import com.digiseq.digiseqwebportal.controller.model.request.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdatePersonnelRequest;
import com.digiseq.digiseqwebportal.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.exception.PersonnelServiceException;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.repository.PersonnelRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonnelService {

  private final PersonnelRepository repository;

  public PersonnelService(PersonnelRepository repository) {
    this.repository = repository;
  }

  public List<Personnel> getPersonnelByClientOrg(long clientOrgId) {
    try {
      return repository.getPersonnelByClientOrg(clientOrgId);
    } catch (Exception e) {
      log.error(
          "Unable to retrieve personnel by client org id: {} due to error: {}",
          clientOrgId,
          e.getMessage());
      throw new PersonnelServiceException("Failed to retrieve personnel", e);
    }
  }

  public Personnel getPersonnelById(long clientOrdId, long personnelId) {
    try {
      return repository
          .getPersonnelById(clientOrdId, personnelId)
          .orElseThrow(() -> new PersonnelNotFoundException("Personnel does not exist"));
    } catch (PersonnelNotFoundException e) {
      log.error(
          "No personnel with clientOrgId: {} and personnelId: {} was found",
          clientOrdId,
          personnelId);
      throw e;
    } catch (Exception e) {
      throw new PersonnelServiceException(
          format(
              "Failed to retrieve personnel with clientOrgId: %s and personnelId: %s",
              clientOrdId, personnelId),
          e);
    }
  }

  public void savePersonnel(AddPersonnelRequest request) {
    Personnel personnel = buildPersonnel(request);
    try {
      repository.savePersonnel(personnel);
    } catch (Exception e) {
      log.error("Failed to save personnel due to error: {}", e.getMessage());
      throw new PersonnelServiceException("Failed to save personnel", e);
    }
  }

  public void updatePersonnel(Long clientOrgId, Long personnelId, UpdatePersonnelRequest request) {
    try {
      Personnel personnel = buildPersonnel(request);
      repository.updatePersonnel(personnel);
    } catch (Exception e) {
      log.error("Failed to update personnel due to error: {}", e.getMessage());
      throw new PersonnelServiceException("Failed to update personnel", e);
    }
  }

  public void deletePersonnel(long clientOrgId, long personnelId) {
    try {
      repository.deletePersonnel(clientOrgId, personnelId);
    } catch (Exception e) {
      throw new PersonnelServiceException(
          format(
              "Failed to delete personnel with clientOrgId: %s and personnelId: %s",
              clientOrgId, personnelId),
          e);
    }
  }

  private static Personnel buildPersonnel(AddPersonnelRequest request) {
    // todo password hashing
    return Personnel.builder()
        .username(request.username())
        .password(request.password())
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .clientOrgId(Long.parseLong(request.clientOrgId()))
        .build();
  }

  private static Personnel buildPersonnel(UpdatePersonnelRequest request) {
    // todo password hashing
    return Personnel.builder()
        .username(request.username())
        .password(request.password())
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .build();
  }
}
