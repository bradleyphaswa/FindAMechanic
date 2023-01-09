package com.android.findamechanic.sparePanel;

public class Spares {

    private String name, email, mobile, street, suburb, service, province;

    public Spares() {
    }

    public Spares(String name, String email, String mobile, String street, String suburb, String service, String province) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.street = street;
        this.suburb = suburb;
        this.service = service;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
