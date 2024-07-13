package com.digiseq.digiseqwebportal.controller;

import static java.lang.Long.parseLong;

import com.digiseq.digiseqwebportal.controller.converter.ClientOrgMapper;
import com.digiseq.digiseqwebportal.controller.model.ClientOrgResponse;
import com.digiseq.digiseqwebportal.controller.validation.ValidClientOrgId;
import com.digiseq.digiseqwebportal.model.ClientOrg;
import com.digiseq.digiseqwebportal.service.ClientOrgService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ClientOrgController {
  private final ClientOrgService clientOrgService;
  private final ClientOrgMapper mapper;

  public ClientOrgController(ClientOrgService clientOrgService, ClientOrgMapper mapper) {
    this.clientOrgService = clientOrgService;
    this.mapper = mapper;
  }

  @GetMapping("/clientOrgs")
  ResponseEntity<List<ClientOrgResponse>> getClientOrgs() {
    List<ClientOrg> clientOrgs = clientOrgService.getClientOrgs();
    List<ClientOrgResponse> response = mapper.toResponse(clientOrgs);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/clientOrgs/{clientOrgId}")
  ResponseEntity<ClientOrgResponse> getClientOrgById(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId) {
    ClientOrg clientOrg = clientOrgService.getClientOrgById(parseLong(clientOrgId));
    ClientOrgResponse response = mapper.toResponse(clientOrg);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/clientOrgs/{clientOrgId}")
  ResponseEntity<Void> deleteClientOrgById(
      @PathVariable(value = "clientOrgId") @ValidClientOrgId String clientOrgId) {
    clientOrgService.deleteClientOrgById(parseLong(clientOrgId));

    return ResponseEntity.noContent().build();
  }
}
