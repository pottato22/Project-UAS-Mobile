package id.ac.umn.jameschristianwira;

import android.graphics.Bitmap;

public class Characters {
    private Bitmap photo;
    private String charName;
    private String realname;
    private String birthday;
    private String gender;

    public Characters(String charName, String realname, String birthday, String gender, Bitmap photo) {
        this.charName = charName;
        this.realname = realname;
        this.birthday = birthday;
        this.gender = gender;
        this.photo = photo;
    }

    public Characters(String charName, String realname, String birthday, String gender) {
        this.charName = charName;
        this.realname = realname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getCharName() {
        return charName;
    }

    public String getRealname() {
        return realname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
