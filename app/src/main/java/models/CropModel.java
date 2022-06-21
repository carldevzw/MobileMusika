package models;

import com.google.firebase.firestore.DocumentId;

public class CropModel {
    private String Name, Price, Location, ImageSrc, ID;
    @DocumentId
    private String documentId;

    public CropModel(String name, String price, String imageSrc, String location, String ID) {
        Name = name;
        Price = price;
        ImageSrc = imageSrc;
        Location= location;
        this.ID= ID;
    }

    public CropModel() {
    }

    public String getID() {
        return ID;
    }

    public String getLocation() {
        return Location;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getImageSrc() {
        return ImageSrc;
    }
}
