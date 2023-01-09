package com.android.findamechanic.sparePanel;

public class SpareAdDetails {

    private String title, description, phone, price, imageUri, randomUid, serviceId;

    public SpareAdDetails() {
    }

    public SpareAdDetails(String title, String description, String price,String phone, String imageUri, String randomUid, String serviceId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.phone = phone;
        this.imageUri = imageUri;
        this.randomUid = randomUid;
        this.serviceId = serviceId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRandomUid() {
        return randomUid;
    }

    public void setRandomUid(String randomUid) {
        this.randomUid = randomUid;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
