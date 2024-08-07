package com.digiseq.digiseqwebportal.personnel.repository;

import static com.digiseq.digiseqwebportal.util.TestDataHelper.PERSONNEL_ID;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.clientOrg;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.personnel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.BaseRepositoryIntegrationTest;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import com.digiseq.digiseqwebportal.clientorg.repository.PostgresClientOrgRepository;
import com.digiseq.digiseqwebportal.clientorg.repository.mapper.ClientOrgRecordMapper;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.personnel.repository.mapper.PersonnelRecordMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostgresPersonnelRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

  private PostgresPersonnelRepository personnelRepository;
  private PostgresClientOrgRepository clientRepository;

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
    Personnel personnel = personnel(savedClientOrg.clientOrgId(), null);

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

    Personnel existingPersonnel = personnel(existingClientOrgId, PERSONNEL_ID);
    personnelRepository.savePersonnel(existingPersonnel);

    Personnel personnel =
        personnelRepository.getPersonnelByClientOrg(existingClientOrgId).getFirst();

    personnelRepository.deletePersonnel(existingClientOrgId, personnel.personnelId());

    Optional<Personnel> retrievedPersonnel =
        personnelRepository.getPersonnelById(existingClientOrgId, personnel.personnelId());
    assertThat(retrievedPersonnel).isEmpty();
  }

  @Test
  void shouldUpdatePersonnel() {
    ClientOrg savedClientOrg = setupExistingClientOrg();
    Long clientOrgId = savedClientOrg.clientOrgId();

    Personnel personnel = personnel(clientOrgId, null);
    personnelRepository.savePersonnel(personnel);

    List<Personnel> personnelList = personnelRepository.getPersonnelByClientOrg(clientOrgId);
    assertThat(personnelList).hasSize(1);
    Personnel savedPersonnel = personnelList.getFirst();

    Personnel newPersonnel =
        Personnel.builder()
            .personnelId(savedPersonnel.personnelId())
            .clientOrgId(clientOrgId)
            .firstName("updatedFname")
            .lastName("updatedLname")
            .username("updatedUsername")
            .password("updatedPassword")
            .email("updatedEmail")
            .phoneNumber("updatedPhoneNumber")
            .build();
    personnelRepository.updatePersonnel(newPersonnel);

    Personnel updatedPersonnel =
        personnelRepository.getPersonnelById(clientOrgId, savedPersonnel.personnelId()).get();

    assertThat(updatedPersonnel.personnelId()).isNotNull();
    assertThat(updatedPersonnel)
        .extracting(
            Personnel::firstName,
            Personnel::lastName,
            Personnel::username,
            Personnel::password,
            Personnel::email,
            Personnel::phoneNumber,
            Personnel::clientOrgId,
            Personnel::personnelId)
        .contains(
            newPersonnel.firstName(),
            newPersonnel.lastName(),
            newPersonnel.username(),
            newPersonnel.password(),
            newPersonnel.email(),
            newPersonnel.phoneNumber(),
            newPersonnel.clientOrgId(),
            savedPersonnel.personnelId());
  }

  private ClientOrg setupExistingClientOrg() {
    clientRepository.saveClientOrg(clientOrg());
    return clientRepository.getClientOrgs().getFirst();
  }
}
