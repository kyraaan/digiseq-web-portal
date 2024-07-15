package com.digiseq.digiseqwebportal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.repository.mapper.ClientOrgRecordMapper;
import com.digiseq.digiseqwebportal.repository.mapper.PersonnelRecordMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostgresPersonnelRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

  private static PostgresPersonnelRepository personnelRepository;
  private static PostgresClientOrgRepository clientRepository;

  @BeforeEach
  void setup() {
    personnelRepository =
        new PostgresPersonnelRepository(dslContext, getMapper(PersonnelRecordMapper.class));
    clientRepository =
        new PostgresClientOrgRepository(dslContext, getMapper(ClientOrgRecordMapper.class));
  }

  @Test
  void shouldSaveAndRetrievePersonnel() {
    ClientOrg savedClientOrg = setupExistingClientOrg();
    Personnel personnel = personnel(savedClientOrg.clientOrgId());

    personnelRepository.savePersonnel(personnel);

    List<Personnel> personnelList =
        personnelRepository.getPersonnelByClientOrg(savedClientOrg.clientOrgId());
    assertThat(personnelList).hasSize(1);
    Personnel retrievedPersonnel = personnelList.getFirst();

    assertThat(retrievedPersonnel.personnelId()).isNotNull();
    assertThat(retrievedPersonnel)
        .extracting(
            Personnel::firstName,
            Personnel::lastName,
            Personnel::username,
            Personnel::password,
            Personnel::email,
            Personnel::phoneNumber,
            Personnel::clientOrgId)
        .contains(
            personnel.firstName(),
            personnel.lastName(),
            personnel.username(),
            personnel.password(),
            personnel.email(),
            personnel.phoneNumber(),
            personnel.clientOrgId());
  }

  @Test
  void shouldDeletePersonnel() {
    ClientOrg existingClientOrg = setupExistingClientOrg();
    Long existingClientOrgId = existingClientOrg.clientOrgId();

    Personnel existingPersonnel = personnel(existingClientOrgId);
    personnelRepository.savePersonnel(existingPersonnel);

    Personnel personnel =
        personnelRepository.getPersonnelByClientOrg(existingClientOrgId).getFirst();

    personnelRepository.deletePersonnel(existingClientOrgId, personnel.personnelId());

    Optional<Personnel> retrievedPersonnel =
        personnelRepository.getPersonnelById(existingClientOrgId, personnel.personnelId());
    assertThat(retrievedPersonnel).isEmpty();
  }

  private static ClientOrg setupExistingClientOrg() {
    ClientOrg clientOrg =
        ClientOrg.builder()
            .clientOrgId(null)
            .name("Test Client")
            .registeredDate(LocalDate.now())
            .expiryDate(LocalDate.now().plusYears(1))
            .isEnabled(true)
            .build();

    clientRepository.saveClientOrg(clientOrg);
    return clientRepository.getClientOrgs().getFirst();
  }

  private static Personnel personnel(Long clientOrgId) {
    return Personnel.builder()
        .firstName("fred")
        .lastName("jones")
        .username("fjones")
        .password("password")
        .email("fjones@email.com")
        .phoneNumber("0123456789")
        .clientOrgId(clientOrgId)
        .build();
  }
}
