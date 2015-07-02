package example.user;

import io.baratine.core.Result;

public interface UserManagerService
{
  void create(String firstName, String lastName, Result<User> result);

  void delete(long id, Result<Boolean> result);
}
