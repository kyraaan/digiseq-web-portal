package com.digiseq.digiseqwebportal.clientorg.repository;

import static com.digiseq.digiseqwebportal.repository.model.public_.tables.Clientorg.CLIENTORG;
import static java.util.Optional.ofNullable;

import com.digiseq.digiseqwebportal.clientorg.model.ClientOrg;
import com.digiseq.digiseqwebportal.clientorg.repository.mapper.ClientOrgRecordMapper;
import com.digiseq.digiseqwebportal.exception.PostgresRepositoryException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

@Slf4j
public class PostgresClientOrgRepository implements ClientOrgRepository {

  private final DSLContext dsl;
  private final ClientOrgRecordMapper mapper;

  public PostgresClientOrgRepository(DSLContext dsl, ClientOrgRecordMapper mapper) {
    this.dsl = dsl;
    this.mapper = mapper;
  }

  @Override
  public List<ClientOrg> getClientOrgs() {
    try {
      return dsl.selectFrom(CLIENTORG).fetch(mapper::fromRecord);
    } catch (Exception e) {
      log.error("Failed to retrieve client orgs due to error: {}", e.getMessage());
      throw new PostgresRepositoryException("Failed to retrieve client orgs");
    }
  }

  @Override
  public Optional<ClientOrg> getClientOrgById(Long clientOrgId) {
    try {
      return ofNullable(
          dsl.selectFrom(CLIENTORG)
              .where(CLIENTORG.CLIENT_ID.eq(clientOrgId.intValue()))
              .fetchOne(mapper::fromRecord));
    } catch (Exception e) {
      log.error(
          "Failed to retrieve client org with id: {} due to error: {}",
          clientOrgId,
          e.getMessage());
      throw new PostgresRepositoryException("Failed to retrieve client org");
    }
  }

  @Override
  public void saveClientOrg(ClientOrg clientOrg) {
    try {
      dsl.insertInto(CLIENTORG, CLIENTORG.NAME, CLIENTORG.REGISTERED_DATE, CLIENTORG.EXPIRY_DATE)
          .values(clientOrg.name(), clientOrg.registeredDate(), clientOrg.expiryDate())
          .execute();
    } catch (Exception e) {
      log.error("Failed to save client org due to error: {}", e.getMessage());
      throw new PostgresRepositoryException("Failed to save client org");
    }
  }

  @Override
  public void updateClientOrg(ClientOrg clientOrg) {
    try {
      dsl.update(CLIENTORG)
          .set(CLIENTORG.NAME, clientOrg.name())
          .set(CLIENTORG.REGISTERED_DATE, clientOrg.registeredDate())
          .set(CLIENTORG.EXPIRY_DATE, clientOrg.expiryDate())
          .where(CLIENTORG.CLIENT_ID.eq(clientOrg.clientOrgId().intValue()))
          .execute();
    } catch (Exception e) {
      log.error(
          "Failed to update client org with id: {} due to error: {}",
          clientOrg.clientOrgId(),
          e.getMessage());
      throw new PostgresRepositoryException("Failed to update client org");
    }
  }

  @Override
  public void deleteClientOrgById(Long clientOrgId) {
    try {
      dsl.deleteFrom(CLIENTORG).where(CLIENTORG.CLIENT_ID.eq(clientOrgId.intValue())).execute();
    } catch (Exception e) {
      log.error(
          "Failed to delete client org with id: {} due to error: {}", clientOrgId, e.getMessage());
      throw new PostgresRepositoryException("Failed to delete client org");
    }
  }
}
