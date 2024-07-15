package com.digiseq.digiseqwebportal.service;

import static com.digiseq.digiseqwebportal.util.TestDataHelper.CLIENT_NAME;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.CLIENT_ORG_ID;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.EXPIRY_DATE;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.REGISTERED_DATE;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.clientOrg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.digiseq.digiseqwebportal.controller.model.request.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdateClientOrgRequest;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ClientOrgServiceTest {
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
  void shouldDeleteClientOrgById() {
    assertDoesNotThrow(() -> service.deleteClientOrgById(CLIENT_ORG_ID));

    verify(repository).deleteClientOrgById(CLIENT_ORG_ID);
  }

  @Test
  void shouldSaveClientOrg() {
    ClientOrg clientOrg = clientOrg(null);

    assertDoesNotThrow(() -> service.saveClientOrg(addClientOrgRequest()));

    verify(repository).saveClientOrg(clientOrg);
  }

  @Test
  void shouldUpdateClientOrg() {
    ClientOrg clientOrg = clientOrg(null);

    assertDoesNotThrow(() -> service.updateClientOrg(CLIENT_ORG_ID, updateClientOrgRequest()));

    verify(repository).updateClientOrg(clientOrg);
  }

  private static AddClientOrgRequest addClientOrgRequest() {
    return AddClientOrgRequest.builder()
        .name(CLIENT_NAME)
        .registeredDate(REGISTERED_DATE)
        .expiryDate(EXPIRY_DATE)
        .build();
  }

  private static UpdateClientOrgRequest updateClientOrgRequest() {
    return UpdateClientOrgRequest.builder()
        .name(CLIENT_NAME)
        .registeredDate(REGISTERED_DATE)
        .expiryDate(EXPIRY_DATE)
        .build();
  }
}
