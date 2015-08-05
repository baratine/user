package example.user;

import javax.inject.Inject;

import io.baratine.core.Lookup;
import io.baratine.core.OnLookup;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceRef;

@Service("public:///user")
public class UserServiceFacade
{
  @Inject @Lookup("/_user")
  private ServiceRef _serviceRef;

  @OnLookup
  public UserService onLookup(String url)
  {
    UserService user = _serviceRef.lookup(url).as(UserService.class);

    return new UserServiceFacadeChild(user);
  }

  public void create(String firstName, String lastName, Result<User> result)
  {
    UserManagerService manager = _serviceRef.as(UserManagerService.class);

    manager.create(firstName, lastName, result);
  }

  static class UserServiceFacadeChild implements UserService {
    private UserService _user;

    public UserServiceFacadeChild(UserService user)
    {
      _user = user;
    }

    public void create(long id, String firstName, String lastName, Result<User> result)
    {
      _user.create(id, firstName, lastName, result);
    }

    public void get(Result<User> result)
    {
      _user.get(result);
    }

    public void setName(String firstName, String lastName, Result<Boolean> result)
    {
      _user.setName(firstName, lastName, result);
    }

    public void delete(Result<Boolean> result)
    {
      _user.delete(result);
    }
  }
}
