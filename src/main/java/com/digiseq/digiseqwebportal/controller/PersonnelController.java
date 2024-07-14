package com.digiseq.digiseqwebportal.controller;

import static java.lang.Long.parseLong;

import com.digiseq.digiseqwebportal.controller.converter.PersonnelMapper;
import com.digiseq.digiseqwebportal.controller.model.request.AddPersonnelRequest;
import com.digiseq.digiseqwebportal.controller.model.request.UpdatePersonnelRequest;
import com.digiseq.digiseqwebportal.controller.model.response.PersonnelResponse;
import com.digiseq.digiseqwebportal.controller.validation.ValidClientOrgId;
import com.digiseq.digiseqwebportal.controller.validation.ValidPersonnelId;
import com.digiseq.digiseqwebportal.model.Personnel;
import com.digiseq.digiseqwebportal.service.PersonnelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PersonnelController {
  private final PersonnelService personnelService;
  private final PersonnelMapper mapper;

  public PersonnelController(PersonnelService personnelService, PersonnelMapper mapper) {
    this.personnelService = personnelService;
    this.mapper = mapper;
  }

  @GetMapping("/clientOrgs/{clientOrgId}/personnel")
  ResponseEntity<List<PersonnelResponse>> getPersonnelByClientOrg(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId) {
    List<Personnel> personnel =
        personnelService.getPersonnelByClientOrg(Long.parseLong(clientOrgId));
    List<PersonnelResponse> response = mapper.toResponse(personnel);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/clientOrgs/{clientOrgId}/personnel/{personnelId}")
  ResponseEntity<PersonnelResponse> getPersonnelById(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId,
      @PathVariable(value = "personnelId") @ValidPersonnelId String personnelId) {
    Personnel personnel =
        personnelService.getPersonnelById(parseLong(clientOrgId), parseLong(personnelId));
    PersonnelResponse response = mapper.toResponse(personnel);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/clientOrgs/{clientOrgId}/personnel")
  ResponseEntity<Void> addPersonnel(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId,
      @RequestBody @Valid AddPersonnelRequest request) {
    personnelService.savePersonnel(request);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/clientOrgs/{clientOrgId}/personnel/{personnelId}")
  ResponseEntity<Void> updatePersonnel(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId,
      @PathVariable(value = "personnelId") @ValidPersonnelId String personnelId,
      @RequestBody @Valid UpdatePersonnelRequest request) {
    personnelService.updatePersonnel(parseLong(clientOrgId), parseLong(personnelId), request);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/clientOrgs/{clientOrgId}/personnel/{personnelId}")
  ResponseEntity<Void> deletePersonnelById(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId,
      @PathVariable(value = "personnelId") @ValidPersonnelId String personnelId) {
    personnelService.deletePersonnel(parseLong(clientOrgId), parseLong(personnelId));

    return ResponseEntity.noContent().build();
  }
}
