package tusba.rhytz.models;

public class User {
    public String id;
    public String username;
    public String mail;
    public String password;
    public String gender;

    public User(String id, String username, String mail, String password, String gender) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
