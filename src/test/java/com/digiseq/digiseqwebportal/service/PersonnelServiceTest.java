package com.digiseq.digiseqwebportal.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.digiseq.digiseqwebportal.controller.model.request.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdatePersonnelRequest;
import com.digiseq.digiseqwebportal.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.exception.PersonnelServiceException;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.repository.PersonnelRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PersonnelServiceTest {
  private static final long CLIENT_ORG_ID = 123L;
  private static final long PERSONNEL_ID = 234L;

  @Mock private PersonnelRepository repository;

  private PersonnelService service;

  @BeforeEach
  void setup() {
    service = new PersonnelService(repository);
  }

  @Test
  void shouldReturnPersonnelByClientOrgId() {
    List<Personnel> expectedPersonnel = List.of(Personnel.builder().build());
    given(repository.getPersonnelByClientOrg(CLIENT_ORG_ID)).willReturn(expectedPersonnel);

    List<Personnel> personnel = service.getPersonnelByClientOrg(CLIENT_ORG_ID);

    assertThat(personnel).isEqualTo(expectedPersonnel);
  }

  @Test
  void shouldReturnEmptyList_givenNoPersonnelFound() {
    given(repository.getPersonnelByClientOrg(CLIENT_ORG_ID)).willReturn(emptyList());

    List<Personnel> personnel = service.getPersonnelByClientOrg(CLIENT_ORG_ID);

    assertThat(personnel).isEmpty();
  }

  @Test
  void shouldThrowServiceExceptionWhenGettingPersonnel_givenRepositoryFailure() {
    given(repository.getPersonnelByClientOrg(CLIENT_ORG_ID))
        .willThrow(new RuntimeException("some error"));

    Throwable error = catchThrowable(() -> service.getPersonnelByClientOrg(CLIENT_ORG_ID));

    assertThat(error)
        .isInstanceOf(PersonnelServiceException.class)
        .hasMessage("Failed to retrieve personnel");
  }

  @Test
  void shouldReturnPersonnelById() {
    Personnel expectedPersonnel = Personnel.builder().build();
    given(repository.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID))
        .willReturn(Optional.of(expectedPersonnel));

    Personnel personnel = service.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID);

    assertThat(personnel).isEqualTo(expectedPersonnel);
  }

  @Test
  void shouldThrowNotFoundException_givenNoPersonnelFound() {
    given(repository.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID)).willReturn(Optional.empty());

    Throwable error = catchThrowable(() -> service.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID));

    assertThat(error)
        .isInstanceOf(PersonnelNotFoundException.class)
        .hasMessage("Personnel does not exist");
  }

  @Test
  void shouldThrowServiceExceptionWhenGettingPersonnelById_givenRepositoryFailure() {
    given(repository.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID))
        .willThrow(new RuntimeException("some error"));

    Throwable error = catchThrowable(() -> service.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID));

    assertThat(error)
        .isInstanceOf(PersonnelServiceException.class)
        .hasMessage("Failed to retrieve personnel with clientOrgId: 123 and personnelId: 234");
  }

  @Test
  void shouldSavePersonnel() {
    assertDoesNotThrow(() -> service.savePersonnel(addPersonnelRequest()));

    verify(repository).savePersonnel(personnel(CLIENT_ORG_ID));
  }

  @Test
  void shouldThrowServiceException_givenFailureToSavePersonnel() {
    doThrow(new RuntimeException("error")).when(repository).savePersonnel(personnel(CLIENT_ORG_ID));

    Throwable error = catchThrowable(() -> service.savePersonnel(addPersonnelRequest()));

    assertThat(error)
        .isInstanceOf(PersonnelServiceException.class)
        .hasMessage("Failed to save personnel");
  }

  @Test
  void shouldUpdatePersonnel() {
    assertDoesNotThrow(
        () -> service.updatePersonnel(CLIENT_ORG_ID, PERSONNEL_ID, updatePersonnelRequest()));

    verify(repository).updatePersonnel(personnel(null));
  }

  @Test
  void shouldThrowServiceException_givenFailureToUpdatePersonnel() {
    doThrow(new RuntimeException("some error")).when(repository).updatePersonnel(personnel(null));

    Throwable error =
        catchThrowable(
            () -> service.updatePersonnel(CLIENT_ORG_ID, PERSONNEL_ID, updatePersonnelRequest()));

    assertThat(error)
        .isInstanceOf(PersonnelServiceException.class)
        .hasMessage("Failed to update personnel");
  }

  @Test
  void shouldDeletePersonnel() {
    assertDoesNotThrow(() -> service.deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID));

    verify(repository).deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID);
  }

  @Test
  void shouldThrowServiceException_givenFailureToDeletePersonnel() {
    doThrow(new RuntimeException("some error"))
        .when(repository)
        .deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID);

    Throwable error = catchThrowable(() -> service.deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID));

    assertThat(error)
        .isInstanceOf(PersonnelServiceException.class)
        .hasMessage("Failed to delete personnel with clientOrgId: 123 and personnelId: 234");
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

  private static AddPersonnelRequest addPersonnelRequest() {
    return AddPersonnelRequest.builder()
        .firstName("fred")
        .lastName("jones")
        .username("fjones")
        .password("password")
        .email("fjones@email.com")
        .phoneNumber("0123456789")
        .clientOrgId("123")
        .build();
  }

  private static UpdatePersonnelRequest updatePersonnelRequest() {
    return UpdatePersonnelRequest.builder()
        .firstName("fred")
        .lastName("jones")
        .username("fjones")
        .password("password")
        .email("fjones@email.com")
        .phoneNumber("0123456789")
        .build();
  }
}
