package dev.tcc.caique.medreport.models;

/**
 * Created by caique on 31/01/16.
 */
public class ProfilePacient {
    private String name;
    private String age;
    private String gender;
    private String stature;
    private String weight;
    private String healthProblem;
    private String allergy;
    private String drugAllergy;
    private String profileUrl;
    private String publicId;

    public ProfilePacient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStature() {
        return stature;
    }

    public void setStature(String stature) {
        this.stature = stature;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHealthProblem() {
        return healthProblem;
    }

    public void setHealthProblem(String healthProblem) {
        this.healthProblem = healthProblem;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getDrugAllergy() {
        return drugAllergy;
    }

    public void setDrugAllergy(String drugAllergy) {
        this.drugAllergy = drugAllergy;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public String toString() {
        return "ProfilePacient{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", stature='" + stature + '\'' +
                ", weight='" + weight + '\'' +
                ", healthProblem='" + healthProblem + '\'' +
                ", allergy='" + allergy + '\'' +
                ", drugAllergy='" + drugAllergy + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", publicId='" + publicId + '\'' +
                '}';
    }
}
