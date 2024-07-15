package com.digiseq.digiseqwebportal.personnel.repository;

import static com.digiseq.digiseqwebportal.repository.model.public_.Tables.PERSONNEL;
import static java.util.Optional.ofNullable;

import com.digiseq.digiseqwebportal.exception.PostgresRepositoryException;
import com.digiseq.digiseqwebportal.personnel.model.Personnel;
import com.digiseq.digiseqwebportal.personnel.repository.mapper.PersonnelRecordMapper;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

@Slf4j
public class PostgresPersonnelRepository implements PersonnelRepository {

  private final DSLContext dsl;
  private final PersonnelRecordMapper mapper;

  public PostgresPersonnelRepository(DSLContext dsl, PersonnelRecordMapper mapper) {
    this.dsl = dsl;
    this.mapper = mapper;
  }

  @Override
  public List<Personnel> getPersonnelByClientOrg(Long clientOrgId) {
    try {
      return dsl.selectFrom(PERSONNEL)
          .where(PERSONNEL.CLIENT_ID.eq(clientOrgId.intValue()))
          .fetch(mapper::fromRecord);
    } catch (Exception e) {
      log.error(
          "Failed to retrieve personnel with clientOrgId: {} due to error: {}",
          clientOrgId,
          e.getMessage());
      throw new PostgresRepositoryException("Failed to retrieve personnel");
    }
  }

  @Override
  public Optional<Personnel> getPersonnelById(Long clientOrgId, Long personnelId) {
    try {
      return ofNullable(
          dsl.selectFrom(PERSONNEL)
              .where(PERSONNEL.PERSONNEL_ID.eq(personnelId.intValue()))
              .fetchOne(mapper::fromRecord));
    } catch (Exception e) {
      log.error(
          "Failed to retrieve personnel with clientOrgId: {} and personnelId: {} due to error: {}",
          clientOrgId,
          personnelId,
          e.getMessage());
      throw new PostgresRepositoryException("Failed to retrieve personnel");
    }
  }

  @Override
  public void deletePersonnel(Long clientOrgId, Long personnelId) {
    try {
      dsl.deleteFrom(PERSONNEL).where(PERSONNEL.PERSONNEL_ID.eq(personnelId.intValue())).execute();
    } catch (Exception e) {
      log.error(
          "Failed to delete personnel with clientOrgId: {} and personnelId: {} due to error: {}",
          clientOrgId,
          personnelId,
          e.getMessage());
      throw new PostgresRepositoryException("Failed to delete personnel");
    }
  }

  @Override
  public void savePersonnel(Personnel personnel) {
    try {
      dsl.insertInto(
              PERSONNEL,
              PERSONNEL.FIRST_NAME,
              PERSONNEL.LAST_NAME,
              PERSONNEL.USERNAME,
              PERSONNEL.PASSWORD,
              PERSONNEL.EMAIL,
              PERSONNEL.PHONE_NUMBER,
              PERSONNEL.CLIENT_ID)
          .values(
              personnel.firstName(),
              personnel.lastName(),
              personnel.username(),
              personnel.password(),
              personnel.email(),
              personnel.phoneNumber(),
              personnel.clientOrgId().intValue())
          .execute();
    } catch (Exception e) {
      log.error("Failed to save personnel due to error: {}", e.getMessage());
      throw new PostgresRepositoryException("Failed to save personnel");
    }
  }

  @Override
  public void updatePersonnel(Personnel personnel) {
    // TODO implement later
  }
}
