package example.user;

import io.baratine.core.Modify;
import io.baratine.core.OnLoad;
import io.baratine.core.OnSave;
import io.baratine.core.Result;
import io.baratine.store.Store;

public class UserServiceImpl implements UserService
{
  private String _url;
  private User _user;

  private transient Store<User> _store;
  private transient ServiceState _state = ServiceState.INVALID;

  public UserServiceImpl(String url, Store<User> store)
  {
    _url = url;

    _store = store;
  }

  public void get(Result<User> result)
  {
    validateState();

    result.complete(_user);
  }

  @Modify
  public void setName(String firstName, String lastName, Result<Boolean> result)
  {
    validateState();

    User user = new User(_user.getId(), firstName, lastName);

    _user = user;

    result.complete(true);
  }

  @Modify
  public void delete(Result<Boolean> result)
  {
    validateState();

    _user = null;

    _state = _state.toInvalid();

    result.complete(true);
  }

  @Modify
  public void create(long id, String firstName, String lastName, Result<User> result)
  {
    _user = new User(id, firstName, lastName);

    _state = _state.toValid();

    result.complete(_user);
  }

  @OnLoad
  public void onLoad(Result<Boolean> result)
  {
    _store.get(_url, user -> {
        if (user != null) {
          _user = user;

          _state = _state.toValid();
        }

        result.complete(true);
    });
  }

  @OnSave
  public void onSave(Result<Boolean> result)
  {
    if (_state == ServiceState.VALID) {
      _store.put(_url, _user, Void -> result.complete(true));
    }
    else {
      _store.remove(_url, Void -> result.complete(true));
    }
  }

  private void validateState()
  {
    if (_state != ServiceState.VALID) {
      throw new IllegalStateException("state is not valid");
    }
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _user + "]";
  }
}
