package sg.edu.rp.c346.changiairportgroup.Chat;

/**
 * Created by 15017523 on 16/6/2017.
 */

public class Users {
    private String name;
    private String photoUrl;
    private String role;

    public Users () {}

    public Users(String name, String photoUrl, String role) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
