package com.example.shoppingapp.Models;

public class categoryModel {
    String productid,productimage,productprice,productname,productdescription ,categoryname,time,date,quantity,uniqueKey;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public categoryModel(String productid, String productimage, String productprice, String productname, String productdescription, String categoryname, String time, String date) {
        this.productid = productid;
        this.productimage = productimage;
        this.productprice = productprice;
        this.productname = productname;
        this.productdescription = productdescription;
        this.categoryname = categoryname;
        this.time = time;
        this.date = date;
    }

    public categoryModel() {

    }

    public String getProductid() {
        return productid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
