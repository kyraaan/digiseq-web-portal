package com.digiseq.digiseqwebportal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.digiseq.digiseqwebportal.controller.model.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.controller.model.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.ClientOrgServiceException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.repository.ClientOrgRepository;
import java.time.LocalDate;
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

  @Test
  void shouldDeleteClientOrgById() {
    assertDoesNotThrow(() -> service.deleteClientOrgById(CLIENT_ORG_ID));

    verify(repository).deleteClientOrgById(CLIENT_ORG_ID);
  }

  @Test
  void shouldThrowServiceExceptionWhenDeletingClientOrg_givenFailureToDelete() {
    doThrow(new RuntimeException("delete error"))
        .when(repository)
        .deleteClientOrgById(CLIENT_ORG_ID);

    Throwable error = catchThrowable(() -> service.deleteClientOrgById(CLIENT_ORG_ID));

    assertThat(error)
        .isInstanceOf(ClientOrgServiceException.class)
        .hasMessage("Failed to delete client org with id: 123");
  }

  @Test
  void shouldSaveClient() {
    ClientOrg clientOrg = clientOrg();

    assertDoesNotThrow(() -> service.saveClientOrg(addClientOrgRequest()));

    verify(repository).saveClientOrg(clientOrg);
  }

  @Test
  void shouldThrowServiceException_givenFailureToSaveClientOrg() {
    doThrow(new RuntimeException("save error")).when(repository).saveClientOrg(clientOrg());

    Throwable error = catchThrowable(() -> service.saveClientOrg(addClientOrgRequest()));

    assertThat(error)
        .isInstanceOf(ClientOrgServiceException.class)
        .hasMessage("Failed to save client org");
  }

  private static ClientOrg clientOrg() {
    return ClientOrg.builder()
        .name("client name 1")
        .registeredDate(LocalDate.of(2020, 7, 21))
        .expiryDate(LocalDate.of(2020, 8, 21))
        .personnel(
            List.of(
                Personnel.builder()
                    .firstName("fred")
                    .lastName("jones")
                    .username("fjones")
                    .password("password")
                    .email("fjones@email.com")
                    .phoneNumber("0123456789")
                    .build()))
        .build();
  }

  private static AddClientOrgRequest addClientOrgRequest() {
    return AddClientOrgRequest.builder()
        .name("client name 1")
        .registeredDate(LocalDate.of(2020, 7, 21))
        .expiryDate(LocalDate.of(2020, 8, 21))
        .personnel(
            List.of(
                AddPersonnelRequest.builder()
                    .firstName("fred")
                    .lastName("jones")
                    .username("fjones")
                    .password("password")
                    .email("fjones@email.com")
                    .phoneNumber("0123456789")
                    .build()))
        .build();
  }
}
