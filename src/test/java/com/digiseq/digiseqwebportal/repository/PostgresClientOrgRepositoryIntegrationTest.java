package com.digiseq.digiseqwebportal.repository;

import static com.digiseq.digiseqwebportal.util.TestDataHelper.clientOrg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.repository.mapper.ClientOrgRecordMapper;
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
        .extracting(
            ClientOrg::name, ClientOrg::registeredDate, ClientOrg::expiryDate, ClientOrg::isEnabled)
        .contains(
            clientOrg.name(),
            clientOrg.registeredDate(),
            clientOrg.expiryDate(),
            clientOrg.isEnabled());
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


}
