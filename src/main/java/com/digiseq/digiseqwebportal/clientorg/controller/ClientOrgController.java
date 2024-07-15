package com.digiseq.digiseqwebportal.clientorg.controller;

import static java.lang.Long.parseLong;

import com.digiseq.digiseqwebportal.clientorg.controller.mapper.ClientOrgMapper;
import com.digiseq.digiseqwebportal.clientorg.controller.model.request.ClientOrgRequest;
import com.digiseq.digiseqwebportal.clientorg.controller.model.response.ClientOrgResponse;
import com.digiseq.digiseqwebportal.clientorg.controller.validation.ValidId;
import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import com.digiseq.digiseqwebportal.clientorg.service.ClientOrgService;
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
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId) {
    ClientOrg clientOrg = clientOrgService.getClientOrgById(parseLong(clientOrgId));
    ClientOrgResponse response = mapper.toResponse(clientOrg);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/clientOrgs")
  ResponseEntity<Void> addClientOrg(@RequestBody @Valid ClientOrgRequest request) {
    clientOrgService.saveClientOrg(request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/clientOrgs/{clientOrgId}")
  ResponseEntity<Void> updateClientOrg(
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId,
      @RequestBody @Valid ClientOrgRequest request) {
    clientOrgService.updateClientOrg(parseLong(clientOrgId), request);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/clientOrgs/{clientOrgId}")
  ResponseEntity<Void> deleteClientOrgById(
      @PathVariable(value = "clientOrgId") @ValidId String clientOrgId) {
    clientOrgService.deleteClientOrgById(parseLong(clientOrgId));

    return ResponseEntity.noContent().build();
  }
}
