package dev.tcc.caique.medreport.models;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Avell B153 MAX on 22/02/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private String type;
    private ProfileMedical pm = new ProfileMedical();
    private ProfilePacient pp = new ProfilePacient();
    private ArrayList<InputStream> inputStreams = new ArrayList<>();
    private String timeStampReport;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ProfileMedical getPm() {
        return pm;
    }

    public void setPm(ProfileMedical pm) {
        this.pm = pm;
    }

    public ProfilePacient getPp() {
        return pp;
    }

    public void setPp(ProfilePacient pp) {
        this.pp = pp;
    }

    public ArrayList<InputStream> getInputStreams() {
        return inputStreams;
    }

    public void setInputStreams(ArrayList<InputStream> inputStreams) {
        this.inputStreams = inputStreams;
    }

    public String getTimeStampReport() {
        return timeStampReport;
    }

    public void setTimeStampReport(String timeStampReport) {
        this.timeStampReport = timeStampReport;
    }
}
