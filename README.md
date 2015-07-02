Example User Service
=====================
This example shows how to create a user service with ID generation.

```java

    import com.caucho.baratine.client.BaratineClient;
    import io.baratine.core.
    
    public static void main(String []args)
      throws Exception
    {
      BaratineClient client = new BaratineClient("http://127.0.0.1:8085/s/pod");
      
      UserManagerServiceSync managerService = client.lookup("/user")
                                                    .as(UserManagerServiceSync.class);
      
      managerService.create("first0", "last0");
      System.out.println(user);
      
      UserServiceSync userService = client.lookup("/user/" + user.getId())
                                          .as(UserServiceSync.class);
      
      User modifiedUser = userService.setName("first1", "last1");
      System.out.println(modifiedUser);
      
      client.close();
    }

```