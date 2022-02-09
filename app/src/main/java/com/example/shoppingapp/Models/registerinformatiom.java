package com.example.shoppingapp.Models;

public class registerinformatiom {
    String name,uid,email,password,image,phonenumber,address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public registerinformatiom() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public registerinformatiom(String name, String uid, String email, String password, String image, String phonenumber) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.image = image;
        this.phonenumber = phonenumber;
    }
}
