package dev.tcc.caique.medreport.models;

/**
 * Created by Avell B153 MAX on 22/02/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }
    private String account[];
    private int typeAccount;
    public String[] getAccount() {
        return account;
    }

    public void setAccount(String[] account) {
        this.account = account;
    }

    private Singleton() {
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }
}
