package dev.tcc.caique.medreport.models;

import java.util.ArrayList;

/**
 * Created by Avell B153 MAX on 22/02/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private int typeAccount;
    private String name;
    private ArrayList<String> friends = new ArrayList<>();

    private Singleton() {
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }
}
