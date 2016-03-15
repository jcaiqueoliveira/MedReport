package dev.tcc.caique.medreport.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by caique on 31/01/16.
 */
public class Report implements Serializable{
    private String title;
    private String description;
    private ArrayList<String> images;

    public Report(String title, String description){//, ArrayList<String> images){
        this.title = title;
        this.description = description;
        this.images = images;
    }
}
