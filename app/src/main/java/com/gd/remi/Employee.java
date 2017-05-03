package com.gd.remi;

/**
 * Created by Rajesh Kumar Dawar on 02-05-2017.
 */

public class Employee {
    int Eid;
    String Name,PhoneNo,Age,Wage,Address,Gender;

    public Employee() {
    }

    public Employee(int eid, String name, String phoneNo, String age, String wage, String address, String gender) {
        Eid = eid;
        Name = name;
        PhoneNo = phoneNo;
        Age = age;
        Wage = wage;
        Address = address;
        Gender = gender;
    }

    public int getEid() {
        return Eid;
    }

    public void setEid(int eid) {
        Eid = eid;
    }

    public  String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getWage() {
        return Wage;
    }

    public void setWage(String wage) {
        Wage = wage;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "Eid=" + Eid +
                ", Name='" + Name + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", Age='" + Age + '\'' +
                ", Wage='" + Wage + '\'' +
                ", Address='" + Address + '\'' +
                ", Gender='" + Gender + '\'' +
                '}';
    }
}
