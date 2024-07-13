package com.digiseq.digiseqwebportal.controller;

import static com.digiseq.digiseqwebportal.util.JsonLoader.loadJsonFromFile;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digiseq.digiseqwebportal.configuration.clientorg.ClientOrgWebConfiguration;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.service.ClientOrgService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClientOrgController.class)
@Import(ClientOrgWebConfiguration.class)
class ClientOrgControllerTest {
  private static final String CLIENT_ORG_URI_PATH = "/clientOrgs";
  private static final String CLIENT_ORG_BY_ID_URI_PATH = "/clientOrgs/{clientOrgId}";
  private static final long CLIENT_ORG_ID = 123L;
  private static final String GET_CLIENT_ORGS_SUCCESS_JSON =
      "responses/get-client-orgs-success-response.json";
  private static final String GET_CLIENT_ORG_BY_ID_SUCCESS_JSON =
      "responses/get-client-org-by-id-success-response.json";
  private static final String INVALID_CLIENT_ORG_ID_RESPONSE_JSON =
      "responses/error/invalid-client-org-id-response.json";
  private static final String CLIENT_ORG_NOT_FOUND_RESPONSE_JSON =
      "responses/error/get-client-org-not-found-response.json";
  private static final String UNKNOWN_ERROR_RESPONSE_JSON =
      "responses/error/unknown-error-response.json";

  @Autowired MockMvc mvc;

  @MockBean ClientOrgService clientOrgService;

  @Test
  void shouldReturnAllClientOrgs() throws Exception {
    given(clientOrgService.getClientOrgs()).willReturn(clientOrgs());

    mvc.perform(get(CLIENT_ORG_URI_PATH))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(loadJsonFromFile(GET_CLIENT_ORGS_SUCCESS_JSON), true));
  }

  @Test
  void shouldThrow500WhenGettingClientOrgs_givenUnknownException() throws Exception {
    given(clientOrgService.getClientOrgs()).willThrow(new RuntimeException("unexpected error"));

    mvc.perform(get(CLIENT_ORG_URI_PATH))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().json(loadJsonFromFile(UNKNOWN_ERROR_RESPONSE_JSON)));
  }

  @Test
  void shouldReturnClientOrgById() throws Exception {
    given(clientOrgService.getClientOrgById(CLIENT_ORG_ID)).willReturn(clientOrg());

    mvc.perform(get(CLIENT_ORG_BY_ID_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(loadJsonFromFile(GET_CLIENT_ORG_BY_ID_SUCCESS_JSON), true));
  }

  @Test
  void shouldThrow400WhenGettingClientOrg_givenInvalidClientOrgId() throws Exception {
    mvc.perform(get(CLIENT_ORG_BY_ID_URI_PATH, "badClientOrgId"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(INVALID_CLIENT_ORG_ID_RESPONSE_JSON)));
  }

  @Test
  void shouldThrow404WhenGettingClientOrgById_givenNoClientOrgFound() throws Exception {
    given(clientOrgService.getClientOrgById(CLIENT_ORG_ID))
        .willThrow(new ClientOrgNotFoundException("Client org not found"));

    mvc.perform(get(CLIENT_ORG_BY_ID_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().json(loadJsonFromFile(CLIENT_ORG_NOT_FOUND_RESPONSE_JSON)));
  }

  @Test
  void shouldDeleteClientOrgById() throws Exception {
    doNothing().when(clientOrgService).deleteClientOrgById(CLIENT_ORG_ID);

    mvc.perform(delete(CLIENT_ORG_BY_ID_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldThrow500WhenDeletingClientOrg_givenUnknownException() throws Exception {
    doThrow(new RuntimeException("unexpected error"))
        .when(clientOrgService)
        .deleteClientOrgById(CLIENT_ORG_ID);

    mvc.perform(delete(CLIENT_ORG_BY_ID_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().json(loadJsonFromFile(UNKNOWN_ERROR_RESPONSE_JSON)));
  }

  private static List<ClientOrg> clientOrgs() {
    return List.of(clientOrg());
  }

  private static ClientOrg clientOrg() {
    return ClientOrg.builder()
        .clientOrgId(CLIENT_ORG_ID)
        .name("client name 1")
        .registeredDate(LocalDate.of(2020, 7, 21))
        .expiryDate(LocalDate.of(2020, 8, 21))
        .isEnabled(true)
        .personnel(
            List.of(
                Personnel.builder()
                    .personnelId(456L)
                    .firstName("fred")
                    .lastName("jones")
                    .username("fjones")
                    .email("fjones@email.com")
                    .phoneNumber("0123456789")
                    .clientOrgId(CLIENT_ORG_ID)
                    .build()))
        .build();
  }
}
