package com.avacado.stupidapps.wanda.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.avacado.stupidapps.wanda.domain.Users;

@Repository
public interface UsersRepo extends CrudRepository<Users, String>
{

  public Users findByApiKey(String apiKey);
  public Users findByUserNameAndPassword(String userName, String password);
}
