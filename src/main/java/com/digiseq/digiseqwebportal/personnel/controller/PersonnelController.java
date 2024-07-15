package com.digiseq.digiseqwebportal.personnel.controller;

import static java.lang.Long.parseLong;

import com.digiseq.digiseqwebportal.clientorg.controller.validation.ValidId;
import com.digiseq.digiseqwebportal.personnel.controller.mapper.PersonnelMapper;
import com.digiseq.digiseqwebportal.personnel.controller.model.request.PersonnelRequest;
import com.digiseq.digiseqwebportal.personnel.controller.model.response.PersonnelResponse;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.personnel.service.PersonnelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId) {
    List<Personnel> personnel = personnelService.getPersonnelByClientOrg(parseLong(clientOrgId));
    List<PersonnelResponse> response = mapper.toResponse(personnel);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/clientOrgs/{clientOrgId}/personnel/{personnelId}")
  ResponseEntity<PersonnelResponse> getPersonnelById(
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId,
      @PathVariable(value = "personnelId") @ValidId String personnelId) {
    Personnel personnel =
        personnelService.getPersonnelById(parseLong(clientOrgId), parseLong(personnelId));
    PersonnelResponse response = mapper.toResponse(personnel);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/clientOrgs/{clientOrgId}/personnel")
  ResponseEntity<Void> addPersonnel(
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId,
      @RequestBody @Valid PersonnelRequest request) {
    personnelService.savePersonnel(request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/clientOrgs/{clientOrgId}/personnel/{personnelId}")
  ResponseEntity<Void> updatePersonnel(
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId,
      @PathVariable(value = "personnelId") @ValidId String personnelId,
      @RequestBody @Valid PersonnelRequest request) {
    personnelService.updatePersonnel(parseLong(clientOrgId), parseLong(personnelId), request);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/clientOrgs/{clientOrgId}/personnel/{personnelId}")
  ResponseEntity<Void> deletePersonnelById(
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId,
      @PathVariable(value = "personnelId") @ValidId String personnelId) {
    personnelService.deletePersonnel(parseLong(clientOrgId), parseLong(personnelId));

    return ResponseEntity.noContent().build();
  }
}
