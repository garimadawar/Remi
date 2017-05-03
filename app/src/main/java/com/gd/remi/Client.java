package com.gd.remi;

/**
 * Created by Rajesh Kumar Dawar on 01-05-2017.
 */

public class Client {
    int Cid;
    String Name, Firm_Name, PhoneNo, Email, Age, Address, City;

    public Client() {

    }

    public Client(int cid, String name, String firm_Name, String phoneNo, String email, String age, String address, String city) {
        Cid = cid;
        Name = name;
        Firm_Name = firm_Name;
        PhoneNo = phoneNo;
        Email = email;
        Age = age;
        Address = address;
        City = city;
    }

    public int getCid() {
        return Cid;
    }

    public void setCid(int cid) {
        Cid = cid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFirm_Name() {
        return Firm_Name;
    }

    public void setFirm_Name(String firm_Name) {
        Firm_Name = firm_Name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    @Override
    public String toString() {
        return "Client{" +
                "Cid=" + Cid +
                ", Name='" + Name + '\'' +
                ", Firm_Name='" + Firm_Name + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", Email='" + Email + '\'' +
                ", Age='" + Age + '\'' +
                ", Address='" + Address + '\'' +
                ", City='" + City + '\'' +
                '}';
    }
}
