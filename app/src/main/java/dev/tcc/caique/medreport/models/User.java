package dev.tcc.caique.medreport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Avell B153 MAX on 22/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String email;
    private String name;

    public User() {

    }

    public User(String name,String email){
        this.email = email;
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
