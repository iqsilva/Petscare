package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 16/10/2017.
 */

public class UserInformation {
    public String id;
    public String doc;
    public String name;
    public String address;
    public String district;
    public String city;
    public String state;
    public String telephone;
    public String email;

    public UserInformation() {
    }

    public UserInformation(String id, String doc, String name, String address, String district, String city, String state, String telephone, String email) {
        this.id = id;
        this.doc = doc;
        this.name = name;
        this.address = address;
        this.district = district;
        this.city = city;
        this.state = state;
        this.telephone = telephone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getDoc() {
        return doc;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }
}
