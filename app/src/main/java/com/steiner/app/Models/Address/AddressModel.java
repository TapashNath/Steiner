package com.steiner.app.Models.Address;

public class AddressModel {
    String mAddressOutput, city, state, district, pinCode, local_address, road_area;

    public AddressModel(String mAddressOutput, String city, String state, String district, String pinCode, String local_address, String road_area) {
        this.mAddressOutput = mAddressOutput;
        this.city = city;
        this.state = state;
        this.district = district;
        this.pinCode = pinCode;
        this.local_address = local_address;
        this.road_area = road_area;
    }

    public String getmAddressOutput() {
        return mAddressOutput;
    }

    public void setmAddressOutput(String mAddressOutput) {
        this.mAddressOutput = mAddressOutput;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getLocal_address() {
        return local_address;
    }

    public void setLocal_address(String local_address) {
        this.local_address = local_address;
    }

    public String getRoad_area() {
        return road_area;
    }

    public void setRoad_area(String road_area) {
        this.road_area = road_area;
    }
}
