package com.digiseq.digiseqwebportal.personnel.controller;

import static com.digiseq.digiseqwebportal.util.JsonLoader.loadJsonFromFile;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.CLIENT_ORG_ID;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.PERSONNEL_ID;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.addPersonnelRequest;
import static com.digiseq.digiseqwebportal.util.TestDataHelper.personnel;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digiseq.digiseqwebportal.personnel.configuration.PersonnelWebConfiguration;
import com.digiseq.digiseqwebportal.personnel.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.personnel.service.PersonnelService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PersonnelController.class)
@Import(PersonnelWebConfiguration.class)
class PersonnelControllerTest {
  private static final String PERSONNEL_URI_PATH = "/clientOrgs/{clientOrgId}/personnel";
  private static final String PERSONNEL_BY_ID_URI_PATH =
      "/clientOrgs/{clientOrgId}/personnel/{personnelId}";

  private static final String GET_PERSONNEL_SUCCESS_JSON =
          "responses/personnel/get-personnel-success-response.json";
  private static final String GET_PERSONNEL_BY_ID_SUCCESS_JSON =
          "responses/personnel/get-personnel-by-id-success-response.json";
  private static final String INVALID_CLIENT_ORG_ID_RESPONSE_JSON =
          "responses/clientorg/error/invalid-client-org-id-response.json";
  private static final String INVALID_PERSONNEL_ID_RESPONSE_JSON =
          "responses/personnel/error/invalid-personnel-id-response.json";
  private static final String PERSONNEL_NOT_FOUND_RESPONSE_JSON =
          "responses/personnel/error/get-personnel-not-found-response.json";
  private static final String UNKNOWN_ERROR_RESPONSE_JSON =
          "responses/unknown-error-response.json";
  private static final String ADD_PERSONNEL_REQUEST_JSON = "requests/personnel/add-personnel-request.json";
  private static final String INVALID_ADD_PERSONNEL_REQUEST_JSON =
          "requests/personnel/invalid-add-personnel-request.json";
  private static final String ADD_PERSONNEL_INVALID_INPUT_RESPONSE_JSON =
          "responses/personnel/error/add-personnel-validation-error-response.json";

  @Autowired MockMvc mvc;

  @MockBean PersonnelService personnelService;

  @Test
  void shouldReturnAllPersonnel() throws Exception {
    given(personnelService.getPersonnelByClientOrg(CLIENT_ORG_ID)).willReturn(personnelList());

    mvc.perform(get(PERSONNEL_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(loadJsonFromFile(GET_PERSONNEL_SUCCESS_JSON), true));
  }

  @Test
  void shouldThrow500WhenGettingPersonnel_givenUnknownException() throws Exception {
    given(personnelService.getPersonnelByClientOrg(CLIENT_ORG_ID))
        .willThrow(new RuntimeException("unexpected error"));

    mvc.perform(get(PERSONNEL_URI_PATH, CLIENT_ORG_ID))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().json(loadJsonFromFile(UNKNOWN_ERROR_RESPONSE_JSON)));
  }

  @Test
  void shouldReturnPersonnelById() throws Exception {
    given(personnelService.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID)).willReturn(personnel());

    mvc.perform(get(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, PERSONNEL_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(loadJsonFromFile(GET_PERSONNEL_BY_ID_SUCCESS_JSON), true));
  }

  @Test
  void shouldThrow400WhenGettingPersonnel_givenInvalidClientOrgId() throws Exception {
    mvc.perform(get(PERSONNEL_BY_ID_URI_PATH, "badClientOrgId", PERSONNEL_ID))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(INVALID_CLIENT_ORG_ID_RESPONSE_JSON)));

    verifyNoInteractions(personnelService);
  }

  @Test
  void shouldThrow400WhenGettingPersonnel_givenInvalidPersonnelId() throws Exception {
    mvc.perform(get(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, "badPersonnelId"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(INVALID_PERSONNEL_ID_RESPONSE_JSON)));

    verifyNoInteractions(personnelService);
  }

  @Test
  void shouldThrow404WhenGettingPersonnelById_givenNoPersonnelFound() throws Exception {
    given(personnelService.getPersonnelById(CLIENT_ORG_ID, PERSONNEL_ID))
        .willThrow(new PersonnelNotFoundException("Personnel not found"));

    mvc.perform(get(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, PERSONNEL_ID))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().json(loadJsonFromFile(PERSONNEL_NOT_FOUND_RESPONSE_JSON)));
  }

  @Test
  void shouldDeletePersonnelById() throws Exception {
    mvc.perform(delete(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, PERSONNEL_ID))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(personnelService).deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID);
  }

  @Test
  void shouldThrow500WhenDeletingPersonnel_givenUnknownException() throws Exception {
    doThrow(new RuntimeException("unexpected error"))
        .when(personnelService)
        .deletePersonnel(CLIENT_ORG_ID, PERSONNEL_ID);

    mvc.perform(delete(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, PERSONNEL_ID))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().json(loadJsonFromFile(UNKNOWN_ERROR_RESPONSE_JSON)));
  }

  @Test
  void shouldAddPersonnel() throws Exception {
    var request = addPersonnelRequest();

    mvc.perform(
            post(PERSONNEL_URI_PATH, CLIENT_ORG_ID)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(ADD_PERSONNEL_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(personnelService).savePersonnel(request);
  }

  @Test
  void shouldThrow400WhenAddingPersonnel_givenInvalidRequest() throws Exception {
    mvc.perform(
            post(PERSONNEL_URI_PATH, CLIENT_ORG_ID)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(INVALID_ADD_PERSONNEL_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(ADD_PERSONNEL_INVALID_INPUT_RESPONSE_JSON)));

    verifyNoInteractions(personnelService);
  }

  @Test
  void shouldUpdatePersonnel() throws Exception {
    var request = addPersonnelRequest();

    mvc.perform(
            put(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, PERSONNEL_ID)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(ADD_PERSONNEL_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(personnelService).updatePersonnel(CLIENT_ORG_ID, PERSONNEL_ID, request);
  }

  @Test
  void shouldThrow400WhenUpdatingPersonnel_givenClientOrgId() throws Exception {
    mvc.perform(
            put(PERSONNEL_BY_ID_URI_PATH, "badClientOrgId", PERSONNEL_ID)
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(ADD_PERSONNEL_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(INVALID_CLIENT_ORG_ID_RESPONSE_JSON)));

    verifyNoInteractions(personnelService);
  }

  @Test
  void shouldThrow400WhenUpdatingPersonnel_givenInvalidPersonnelId() throws Exception {
    mvc.perform(
            put(PERSONNEL_BY_ID_URI_PATH, CLIENT_ORG_ID, "badPersonnelId")
                .contentType(APPLICATION_JSON)
                .content(loadJsonFromFile(ADD_PERSONNEL_REQUEST_JSON)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(loadJsonFromFile(INVALID_PERSONNEL_ID_RESPONSE_JSON)));

    verifyNoInteractions(personnelService);
  }

  private static List<Personnel> personnelList() {
    return List.of(personnel());
  }
}
