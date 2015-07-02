package example.user;

public interface UserManagerServiceSync
{
  User create(String firstName, String lastName);

  boolean delete(long id);
}
