package example.user;

public enum ServiceState
{
  VALID {
  },
  INVALID {
  };

  public ServiceState toValid()
  {
    return VALID;
  }

  public ServiceState toInvalid()
  {
    return INVALID;
  }
}
