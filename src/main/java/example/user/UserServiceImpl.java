package example.user;

import javax.inject.Inject;

import io.baratine.core.Lookup;
import io.baratine.core.Modify;
import io.baratine.core.OnLoad;
import io.baratine.core.OnSave;
import io.baratine.core.Result;
import io.baratine.store.Store;

public class UserServiceImpl implements UserService
{
  private String _url;

  private User _user;

  @Inject @Lookup("store://")
  private transient Store<User> _store;

  public UserServiceImpl(String url)
  {
    _url = url;
  }

  public void get(Result<User> result)
  {
    if (_user != null) {
      result.complete(_user);
    }
    else {
      result.complete(null);
    }
  }

  @Modify
  public void setName(String firstName, String lastName, Result<Boolean> result)
  {
    if (_user != null) {
      User user = new User(_user.getId(), firstName, lastName);

      _user = user;

      result.complete(true);
    }
    else {
      result.complete(false);
    }
  }

  @Modify
  public void delete(Result<Boolean> result)
  {
    _user = null;
  }

  @Modify
  public void create(long id, String firstName, String lastName, Result<User> result)
  {
    _user = new User(id, firstName, lastName);

    result.complete(_user);
  }

  @OnLoad
  public void onLoad(Result<Boolean> result)
  {
    _store.get(_url, user -> {
        if (user != null) {
          _user = user;
        }
    });
  }

  @OnSave
  public void onSave(Result<Boolean> result)
  {
    if (_user != null) {
      _store.put(_url, _user, Void -> result.complete(true));
    }
    else {
      _store.remove(_url, Void -> result.complete(true));
    }
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _user + "]";
  }
}
