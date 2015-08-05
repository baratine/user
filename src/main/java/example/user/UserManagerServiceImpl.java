package example.user;

import javax.inject.Inject;

import io.baratine.core.Lookup;
import io.baratine.core.Modify;
import io.baratine.core.OnLoad;
import io.baratine.core.OnLookup;
import io.baratine.core.OnSave;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceRef;
import io.baratine.store.Store;

@Service("/_user")
public class UserManagerServiceImpl implements UserManagerService
{
  @Inject @Lookup("/_user")
  private transient ServiceRef _meRef;

  @Inject @Lookup("store://_user")
  private transient ServiceRef _storeRef;

  private long _count;

  @OnLookup
  public UserService onLookup(String url)
  {
    Store<User> userStore = _storeRef.lookup(url).as(Store.class);

    return new UserServiceImpl(url, userStore);
  }

  @Modify
  public void create(String firstName, String lastName,
                     Result<User> result)
  {
    long id = _count++;

    UserService user = _meRef.lookup("/" + id).as(UserService.class);

    user.create(id, firstName, lastName, result);
  }

  public void delete(long id, Result<Boolean> result)
  {
    UserService user = _meRef.lookup("/" + id).as(UserService.class);

    user.delete(result);
  }

  @OnLoad
  public void onLoad(Result<Boolean> result)
  {
    Store<Long> store = _storeRef.as(Store.class);

    store.get("count", count -> {
       if (count == null) {
         count = 0L;
       }

       _count = count;

       result.complete(true);
    });
  }

  @OnSave
  public void onSave(Result<Boolean> result)
  {
    Store<Long> store = _storeRef.as(Store.class);

    store.put("count", _count, Void -> result.complete(true));
  }
}
