package com.digiseq.digiseqwebportal.util;

import static com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus.ACTIVE;

import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus;
import com.digiseq.digiseqwebportal.personnel.controller.model.request.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.personnel.controller.model.request.UpdatePersonnelRequest;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import java.time.LocalDate;

public class TestDataHelper {
  public static final String CLIENT_NAME = "client name 1";
  public static final long CLIENT_ORG_ID = 123L;
  public static final LocalDate REGISTERED_DATE = LocalDate.of(2020, 7, 21);
  public static final LocalDate EXPIRY_DATE = LocalDate.of(2020, 8, 21);
  public static final long PERSONNEL_ID = 234L;
  public static final String FIRST_NAME = "fred";
  public static final String LAST_NAME = "jones";
  public static final String USERNAME = "fjones";
  public static final String PASSWORD = "encodedPassword";
  public static final String EMAIL = "fjones@email.com";
  public static final String NUMBER = "0123456789";

  public static ClientOrg clientOrg() {
    return clientOrg(CLIENT_ORG_ID, ACTIVE);
  }

  public static ClientOrg clientOrg(Long clientOrgId) {
    return clientOrg(clientOrgId, ACTIVE);
  }

  public static ClientOrg clientOrg(Long clientOrgId, ClientOrgStatus status) {
    return ClientOrg.builder()
        .clientOrgId(clientOrgId)
        .name(CLIENT_NAME)
        .registeredDate(REGISTERED_DATE)
        .expiryDate(EXPIRY_DATE)
        .status(status)
        .build();
  }

  public static Personnel personnel() {
    return personnel(CLIENT_ORG_ID, PERSONNEL_ID);
  }

  public static Personnel personnel(Long clientOrgId, Long personnelId) {
    return Personnel.builder()
        .personnelId(personnelId)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .username(USERNAME)
        .password(PASSWORD)
        .email(EMAIL)
        .phoneNumber(NUMBER)
        .clientOrgId(clientOrgId)
        .build();
  }

  public static AddPersonnelRequest addPersonnelRequest() {
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

  public static UpdatePersonnelRequest updatePersonnelRequest() {
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
