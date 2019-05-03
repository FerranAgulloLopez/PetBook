package service.main.entity.input_output;

import service.main.entity.Localization;

import java.io.Serializable;

public class DataLocalization implements Serializable {

    private String address;
    private double latitude;
    private double longitude;


    /*
    Get
     */

    public DataLocalization() {}

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    /*
    Auxiliary operations
     */

    public Localization toLocalization() {
        return new Localization(address,latitude,longitude);
    }
}
