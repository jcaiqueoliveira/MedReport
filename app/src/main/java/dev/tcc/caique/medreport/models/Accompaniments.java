package dev.tcc.caique.medreport.models;

import java.io.Serializable;

/**
 * Created by Avell B153 MAX on 06/03/2016.
 */
public class Accompaniments implements Serializable {
    private  String name;
    private String img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
