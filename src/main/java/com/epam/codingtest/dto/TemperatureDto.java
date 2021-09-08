package com.epam.codingtest.dto;

public class TemperatureDto {

    public TemperatureDto (String province,String city,String county){
        this.province = province;
        this.city = city;
        this.county = county;
    }

    private String province;//省

    private String city;//市

    private String county;//區

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
