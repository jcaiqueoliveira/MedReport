package dev.tcc.caique.medreport.models;

/**
 * Created by caique on 29/01/16.
 */
public class ProfileMedical {
    private String name;
    private String crm;
    private String specialization;

    public ProfileMedical() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "ProfileMedical{" +
                "name='" + name + '\'' +
                ", crm='" + crm + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
