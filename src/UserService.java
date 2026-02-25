import java.util.*;

class UserService {
    private Map<String, User> users = new HashMap<>();

    public void createUser(String id, String name) {
        if (users.containsKey(id)) {
            System.out.println("User already exists!");
            return;
        }
        users.put(id, new User(id, name));
    }

    
    public User getUser(String id) {
        return users.get(id);
    }


    public void updateUser(String id, String name) {
        User user = users.get(id);
        if (user != null) {
            user.setName(name);
        }
    }

 
    public void deleteUser(String id) {
        users.remove(id);
    }

  
    public void showUsers() {
        for (User u : users.values()) {
            System.out.println(u.getId() + " " + u.getName());
        }
    }
}