package dev.tcc.caique.medreport.models;

/**
 * Caique Oliveira on 27/03/2016.
 */
public class Image {
    private String image;
    private String publicId;
    public Image() {

    }
    public Image(String image, String publicId){
        this.image = image;
        this.publicId = publicId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }



    @Override
    public String toString() {
        return "Image{" +
                "image='" + image + '\'' +
                ", publicId='" + publicId + '\'' +
                '}';
    }
}
