package com.gd.remi;

/**
 * Created by Rajesh Kumar Dawar on 03-05-2017.
 */

public class Product {
    int Pid;
    String Name,Price,Desc;

    public Product(){

    }
    public Product(int pid, String name, String price, String desc) {
        Pid = pid;
        Name = name;
        Price = price;
        Desc = desc;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Pid=" + Pid +
                ", Name='" + Name + '\'' +
                ", Price='" + Price + '\'' +
                ", Desc='" + Desc + '\'' +
                '}';
    }
}
