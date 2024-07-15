package com.digiseq.digiseqwebportal.personnel.service;

import static com.digiseq.digiseqwebportal.util.TestDataHelper.CLIENT_ORG_ID;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.PERSONNEL_ID;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.addPersonnelRequest;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.personnel;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.digiseq.digiseqwebportal.personnel.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.personnel.repository.PersonnelRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PersonnelServiceTest {

  @Mock private PersonnelRepository repository;
  @Mock private PasswordEncoder passwordEncoder;

  private PersonnelService service;

  @BeforeEach
  void setup() {
    service = new PersonnelService(repository, passwordEncoder);
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
  void shouldSavePersonnel() {
    given(passwordEncoder.encode("password")).willReturn("encodedPassword");

    assertDoesNotThrow(() -> service.savePersonnel(addPersonnelRequest()));

    verify(repository).savePersonnel(personnel(CLIENT_ORG_ID, null));
  }

  @Test
  void shouldUpdatePersonnel() {
    given(passwordEncoder.encode("password")).willReturn("encodedPassword");

    assertDoesNotThrow(
        () -> service.updatePersonnel(CLIENT_ORG_ID, PERSONNEL_ID, addPersonnelRequest()));

    verify(repository).updatePersonnel(personnel(CLIENT_ORG_ID, PERSONNEL_ID));
  }

  @Test
  void shouldDeletePersonnel() {
    assertDoesNotThrow(() -> service.deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID));

    verify(repository).deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID);
  }
}
