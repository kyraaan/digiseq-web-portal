package com.digiseq.digiseqwebportal.repository;

import com.digiseq.digiseqwebportal.model.Personnel;
import java.util.List;
import java.util.Optional;

public interface PersonnelRepository {

  List<Personnel> getPersonnelByClientOrg(Long clientOrgId);

  Optional<Personnel> getPersonnelById(Long clientOrgId, Long personnelId);

  void deletePersonnel(Long clientOrgId, Long personnelId);

  void savePersonnel(Personnel personnel);

  void updatePersonnel(Personnel personnel);
}
