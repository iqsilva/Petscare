package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 19/09/2017.
 */

public class Usuario {

     public String id;
     public String doc;
     public String name;
     public int userType;
     public String address;
     public String district;
     public String city;
     public String state;
     public String telephone;

    public Usuario(){}

    public Usuario(String id, String doc, String name, int userType, String address, String district, String city, String state, String telephone) {
        this.id = id;
        this.doc = doc;
        this.name = name;
        this.userType = userType;
        this.address = address;
        this.district = district;
        this.city = city;
        this.state = state;
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


}
