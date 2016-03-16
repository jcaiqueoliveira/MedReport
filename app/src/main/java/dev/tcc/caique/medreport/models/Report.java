package dev.tcc.caique.medreport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by caique on 31/01/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Report {
    private String title;
    private String description;
    long stackId;
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

    public long getStackId() {
        return stackId;
    }

    public void setStackId(long stackId) {
        this.stackId = stackId;
    }
}
