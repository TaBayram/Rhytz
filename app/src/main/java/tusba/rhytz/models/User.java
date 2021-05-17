package tusba.rhytz.models;

public class User {
    String Id;
    String Name;
    String Surname;
    String Email;
    String Username;
    String Password;
    String Gender;

    public User(){}
    public User(String id, String name, String surname, String email, String username, String password, String gender) {
        Id = id;
        Name = name;
        Surname = surname;
        Email = email;
        Username = username;
        Password = password;
        Gender = gender;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}