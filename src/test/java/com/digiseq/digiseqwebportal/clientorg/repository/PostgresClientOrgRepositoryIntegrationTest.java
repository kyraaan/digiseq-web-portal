package com.digiseq.digiseqwebportal.clientorg.repository;

import static com.digiseq.digiseqwebportal.util.TestDataHelper.EXPIRY_DATE;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.REGISTERED_DATE;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.clientOrg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.BaseRepositoryIntegrationTest;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import com.digiseq.digiseqwebportal.clientorg.repository.mapper.ClientOrgRecordMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostgresClientOrgRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

  private PostgresClientOrgRepository repository;

  @BeforeEach
  void setup() {
    repository =
        new PostgresClientOrgRepository(dslContext, getMapper(ClientOrgRecordMapper.class));
  }

  @Test
  void shouldSaveAndRetrieveClientOrg() {
    ClientOrg clientOrg = clientOrg();

    repository.saveClientOrg(clientOrg);

    List<ClientOrg> clientOrgs = repository.getClientOrgs();
    assertThat(clientOrgs).hasSize(1);
    ClientOrg savedClientOrg = clientOrgs.getFirst();

    assertThat(savedClientOrg.clientOrgId()).isNotNull();
    assertThat(savedClientOrg)
        .extracting(ClientOrg::name, ClientOrg::registeredDate, ClientOrg::expiryDate)
        .contains(clientOrg.name(), clientOrg.registeredDate(), clientOrg.expiryDate());
  }

  @Test
  void shouldDeleteClientOrg() {
    ClientOrg clientOrg = clientOrg();
    repository.saveClientOrg(clientOrg);

    Long savedClientOrgId = repository.getClientOrgs().getFirst().clientOrgId();
    repository.deleteClientOrgById(savedClientOrgId);

    Optional<ClientOrg> retrievedClientOrgOpt = repository.getClientOrgById(savedClientOrgId);
    assertThat(retrievedClientOrgOpt).isEmpty();
  }

  @Test
  void shouldUpdateClientOrg() {
    ClientOrg savedClientOrg = setupExistingClientOrg();
    Long clientOrgId = savedClientOrg.clientOrgId();

    ClientOrg newClientOrg =
        ClientOrg.builder()
            .clientOrgId(clientOrgId)
            .name("updatedName")
            .registeredDate(REGISTERED_DATE.plusDays(2))
            .expiryDate(EXPIRY_DATE.plusDays(2))
            .build();

    repository.updateClientOrg(newClientOrg);

    ClientOrg updatedClientOrg = repository.getClientOrgById(clientOrgId).get();

    assertThat(updatedClientOrg)
        .extracting(ClientOrg::name, ClientOrg::registeredDate, ClientOrg::expiryDate)
        .contains(newClientOrg.name(), newClientOrg.registeredDate(), newClientOrg.expiryDate());
  }

  private ClientOrg setupExistingClientOrg() {
    repository.saveClientOrg(clientOrg());
    return repository.getClientOrgs().getFirst();
  }
}
