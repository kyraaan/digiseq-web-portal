package com.digiseq.digiseqwebportal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.ClientOrgServiceException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ClientOrgServiceTest {
  private static final long CLIENT_ORG_ID = 123L;

  @Mock private ClientOrgRepository repository;

  private ClientOrgService service;

  @BeforeEach
  void setup() {
    service = new ClientOrgService(repository);
  }

  @Test
  void shouldGetAllClientOrgs() {
    List<ClientOrg> savedClientOrgs =
        List.of(ClientOrg.builder().clientOrgId(CLIENT_ORG_ID).build());
    given(repository.getClientOrgs()).willReturn(savedClientOrgs);

    List<ClientOrg> clientOrgs = service.getClientOrgs();

    assertThat(clientOrgs).isEqualTo(savedClientOrgs);
  }

  @Test
  void shouldReturnEmptyList_givenNoClientOrgsInRepository() {
    given(repository.getClientOrgs()).willReturn(Collections.emptyList());

    List<ClientOrg> clientOrgs = service.getClientOrgs();

    assertThat(clientOrgs).isEmpty();
  }

  @Test
  void shouldReturnException_givenFailureToRetrieveClientOrgs() {
    given(repository.getClientOrgs()).willThrow(new RuntimeException("some error"));

    Throwable error = catchThrowable(() -> service.getClientOrgs());

    assertThat(error)
        .isInstanceOf(ClientOrgServiceException.class)
        .hasMessage("Failed to retrieve client orgs");
  }

  @Test
  void shouldGetClientOrgById() {
    ClientOrg savedClientOrg = ClientOrg.builder().clientOrgId(CLIENT_ORG_ID).build();
    given(repository.getClientOrgById(CLIENT_ORG_ID)).willReturn(Optional.of(savedClientOrg));

    ClientOrg clientOrg = service.getClientOrgById(CLIENT_ORG_ID);

    assertThat(clientOrg).isEqualTo(savedClientOrg);
  }

  @Test
  void shouldThrowNotFoundException_givenNoClientOrgFoundInRepository() {
    given(repository.getClientOrgById(CLIENT_ORG_ID)).willReturn(Optional.empty());

    Throwable error = catchThrowable(() -> service.getClientOrgById(CLIENT_ORG_ID));

    assertThat(error)
        .isInstanceOf(ClientOrgNotFoundException.class)
        .hasMessage("Client org with id: 123 does not exist");
  }

  @Test
  void shouldThrowServiceException_givenFailureToRetrieveClientOrgById() {
    given(repository.getClientOrgById(CLIENT_ORG_ID)).willThrow(new RuntimeException("some error"));

    Throwable error = catchThrowable(() -> service.getClientOrgById(CLIENT_ORG_ID));

    assertThat(error)
        .isInstanceOf(ClientOrgServiceException.class)
        .hasMessage("Failed to retrieve client org with id: 123");
  }
}
