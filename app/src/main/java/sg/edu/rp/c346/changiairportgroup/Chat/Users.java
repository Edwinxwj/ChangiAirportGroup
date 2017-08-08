package sg.edu.rp.c346.changiairportgroup.Chat;

/**
 * Created by 15017523 on 16/6/2017.
 */

public class Users {
    private String Name;
    private String Image;
    private String Role;

    public Users () {}

    public Users(String name, String photoUrl, String role) {
        this.Name = name;
        this.Image = photoUrl;
        this.Role = role;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String photoUrl) {
        this.Image = photoUrl;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        this.Role = role;
    }
}
