package com.digiseq.digiseqwebportal.personnel.service;

import static java.lang.Long.parseLong;

import com.digiseq.digiseqwebportal.personnel.controller.model.request.PersonnelRequest;
import com.digiseq.digiseqwebportal.personnel.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.personnel.repository.PersonnelRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PersonnelService {

  private final PersonnelRepository repository;
  private final PasswordEncoder passwordEncoder;

  public PersonnelService(PersonnelRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<Personnel> getPersonnelByClientOrg(Long clientOrgId) {
    return repository.getPersonnelByClientOrg(clientOrgId);
  }

  public Personnel getPersonnelById(Long clientOrdId, Long personnelId) {
    return repository
        .getPersonnelById(clientOrdId, personnelId)
        .orElseThrow(() -> new PersonnelNotFoundException("Personnel does not exist"));
  }

  public void savePersonnel(PersonnelRequest request) {
    Long clientOrgId = parseLong(request.clientOrgId());
    Personnel personnelToSave = buildPersonnel(request, clientOrgId, null);
    repository.savePersonnel(personnelToSave);
  }

  public void updatePersonnel(Long clientOrgId, Long personnelId, PersonnelRequest request) {
    Personnel personnelToUpdate = buildPersonnel(request, clientOrgId, personnelId);
    repository.updatePersonnel(personnelToUpdate);
  }

  public void deletePersonnel(Long clientOrgId, Long personnelId) {
    repository.deletePersonnel(clientOrgId, personnelId);
  }

  private Personnel buildPersonnel(
          PersonnelRequest request, Long clientOrgId, Long personnelId) {
    String encodedPassword = passwordEncoder.encode(request.password());
    return Personnel.builder()
        .username(request.username())
        .password(encodedPassword)
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .clientOrgId(clientOrgId)
        .personnelId(personnelId)
        .build();
  }
}
