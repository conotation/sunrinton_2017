package cf.connota.sunrinton.Model;

/**
 * Created by Conota on 2017-07-25.
 */

public class User {
    private int id;
    private String nickname = "";
    private String profile = "";
    public boolean isActivate = false;

    public User(String nickname, String profile, boolean isActivate, int id) {
        this.nickname = nickname;
        this.profile = profile;
        this.isActivate = isActivate;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile() {
        return profile;
    }


    public int getId() {
        return id;
    }
}
