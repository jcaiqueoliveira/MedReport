package dev.tcc.caique.medreport.models;

/**
 * Created by Caique on 26/03/2016.
 */
public class Invite {
    private String name;
    private String user;
    private String stackId;
    private String photo;

    public Invite() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return user;
    }

    public void setEmail(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", stackId='" + stackId + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
