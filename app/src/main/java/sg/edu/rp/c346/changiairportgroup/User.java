package sg.edu.rp.c346.changiairportgroup;

import java.io.Serializable;

/**
 * Created by 15017167 on 7/8/2017.
 */

public class User implements Serializable {

    private String name;
    private String image;
    private String role;

    public User() {
    }

    public User(String name, String image, String role) {
        this.name = name;
        this.image = image;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
