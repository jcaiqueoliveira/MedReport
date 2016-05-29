package dev.tcc.caique.medreport.models;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Caique Oliveira on 22/02/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private String type;
    private ProfileMedical pm = new ProfileMedical();
    private ProfilePacient pp = new ProfilePacient();
    private ArrayList<InputStream> inputStreams = new ArrayList<>();
    private String timeStampReport;
    private int typeAccount;
    private String name;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<Image> imageToDeleteCloudinary = new ArrayList<>();
    private ArrayList<Image> currentImageInReport = new ArrayList<>();
    private String urlToShow;
    public static Singleton getInstance() {
        return ourInstance;
    }
    public static void resetInstance(){
        ourInstance = null;
    }
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

    public ArrayList<Image> getImageToDeleteCloudinary() {
        return imageToDeleteCloudinary;
    }

    public void setImageToDeleteCloudinary(ArrayList<Image> imageToDeleteCloudinary) {
        this.imageToDeleteCloudinary = imageToDeleteCloudinary;
    }

    public ArrayList<Image> getCurrentImageInReport() {
        return currentImageInReport;
    }

    public void setCurrentImageInReport(ArrayList<Image> currentImageInReport) {
        this.currentImageInReport = currentImageInReport;
    }

    public String getUrlToShow() {
        return urlToShow;
    }

    public void setUrlToShow(String urlToShow) {
        this.urlToShow = urlToShow;
    }
}
