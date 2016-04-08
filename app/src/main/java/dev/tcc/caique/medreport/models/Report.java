package dev.tcc.caique.medreport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by caique on 31/01/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Report implements Serializable{
    private String title;
    private String description;
    private String stackId;
    public Report(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }
}
