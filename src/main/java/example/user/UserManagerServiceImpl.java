package example.user;

import io.baratine.core.Modify;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.Services;

@Service("public:///user")
public class UserManagerServiceImpl implements UserManagerService
{
  private long _count;

  @Modify
  public void create(String firstName, String lastName,
                     Result<User> result)
  {
    long id = _count++;

    UserServiceImpl user = getUserProxy("/user/" + id);

    user.create(id, firstName, lastName, result);
  }

  public void delete(long id, Result<Boolean> result)
  {
    UserServiceImpl user = getUserProxy("/user/" + id);

    user.delete(result);
  }

  private UserServiceImpl getUserProxy(String url)
  {
    UserServiceImpl user = Services.getCurrentService().lookup(url).as(UserServiceImpl.class);

    return user;
  }
}
