package dev.tcc.caique.medreport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Avell B153 MAX on 19/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Inviter {
    private String inviter;
    long stackId;
    public Inviter(){

    }

    public String getUid() {
        return inviter;
    }

    public void setUid(String inviter) {
        this.inviter = inviter;
    }

    public long getStackId() {
        return stackId;
    }

    public void setStackId(long stackId) {
        this.stackId = stackId;
    }
}
