package com.digiseq.digiseqwebportal.controller;

import static com.digiseq.digiseqwebportal.util.JsonLoader.loadJsonFromFile;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digiseq.digiseqwebportal.configuration.clientorg.ClientOrgWebConfiguration;
import com.digiseq.digiseqwebportal.controller.model.request.AddClientOrgRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdateClientOrgRequest;
import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.model.ClientOrg;
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
  private static final String CLIENT_NAME = "client name 1";
  private static final long CLIENT_ORG_ID = 123L;
  private static final LocalDate REGISTERED_DATE = LocalDate.of(2020, 7, 21);
  private static final LocalDate EXPIRY_DATE = LocalDate.of(2020, 8, 21);

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
  private static final String ADD_CLIENT_ORG_REQUEST_JSON = "requests/add-client-org-request.json";
  private static final String UPDATE_CLIENT_ORG_REQUEST_JSON =
      "requests/update-client-org-request.json";
  private static final String INVALID_ADD_CLIENT_ORG_REQUEST_JSON =
      "requests/invalid-add-client-org-request.json";
  private static final String ADD_CLIENT_ORG_INVALID_INPUT_RESPONSE_JSON =
      "responses/error/add-client-org-validation-error-response.json";

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

    verifyNoInteractions(clientOrgService);
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
    mvc.perform(delete(CLIENT_ORG_BY_ID_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(clientOrgService).deleteClientOrgById(CLIENT_ORG_ID);
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

  @Test
  void shouldAddClientOrg() throws Exception {
    var request =
        AddClientOrgRequest.builder()
            .name(CLIENT_NAME)
            .registeredDate(REGISTERED_DATE)
            .expiryDate(EXPIRY_DATE)
            .build();
    mvc.perform(
            post(CLIENT_ORG_URI_PATH)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(ADD_CLIENT_ORG_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(clientOrgService).saveClientOrg(request);
  }

  @Test
  void shouldThrow400WhenAddingClientOrg_givenInvalidRequest() throws Exception {
    mvc.perform(
            post(CLIENT_ORG_URI_PATH)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(INVALID_ADD_CLIENT_ORG_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(ADD_CLIENT_ORG_INVALID_INPUT_RESPONSE_JSON)));

    verifyNoInteractions(clientOrgService);
  }

  @Test
  void shouldUpdateClientOrg() throws Exception {
    var request =
        UpdateClientOrgRequest.builder()
            .name(CLIENT_NAME)
            .registeredDate(REGISTERED_DATE)
            .expiryDate(EXPIRY_DATE)
            .build();

    mvc.perform(
            patch(CLIENT_ORG_BY_ID_URI_PATH, CLIENT_ORG_ID)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(UPDATE_CLIENT_ORG_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(clientOrgService).updateClientOrg(CLIENT_ORG_ID, request);
  }

  @Test
  void shouldThrow400WhenUpdatingClientOrg_givenInvalidClientOrgId() throws Exception {
    mvc.perform(
            patch(CLIENT_ORG_BY_ID_URI_PATH, "badClientOrgId")
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(UPDATE_CLIENT_ORG_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(INVALID_CLIENT_ORG_ID_RESPONSE_JSON)));

    verifyNoInteractions(clientOrgService);
  }

  private static List<ClientOrg> clientOrgs() {
    return List.of(clientOrg());
  }

  private static ClientOrg clientOrg() {
    return ClientOrg.builder()
        .clientOrgId(CLIENT_ORG_ID)
        .name(CLIENT_NAME)
        .registeredDate(REGISTERED_DATE)
        .expiryDate(EXPIRY_DATE)
        .isEnabled(true)
        .build();
  }
}
