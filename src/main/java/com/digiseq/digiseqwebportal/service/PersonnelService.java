package com.digiseq.digiseqwebportal.service;

import com.digiseq.digiseqwebportal.controller.model.request.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdatePersonnelRequest;
import com.digiseq.digiseqwebportal.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.repository.PersonnelRepository;
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

  public void savePersonnel(AddPersonnelRequest request) {
    Personnel personnel = buildPersonnel(request);
    repository.savePersonnel(personnel);
  }

  public void updatePersonnel(Long clientOrgId, Long personnelId, UpdatePersonnelRequest request) {
    Personnel personnel = buildPersonnel(request);
    repository.updatePersonnel(personnel);
  }

  public void deletePersonnel(Long clientOrgId, Long personnelId) {
    repository.deletePersonnel(clientOrgId, personnelId);
  }

  private  Personnel buildPersonnel(AddPersonnelRequest request) {
    String encodedPassword = passwordEncoder.encode(request.password());
    return Personnel.builder()
        .username(request.username())
        .password(encodedPassword)
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .clientOrgId(Long.parseLong(request.clientOrgId()))
        .build();
  }

  private Personnel buildPersonnel(UpdatePersonnelRequest request) {
    String encodedPassword = passwordEncoder.encode(request.password());
    return Personnel.builder()
        .username(request.username())
        .password(encodedPassword)
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .build();
  }
}
