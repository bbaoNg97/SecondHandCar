package my.edu.tarc.secondhandcar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by bbao_Ng on 20/11/2018.
 */

public class Car{

    private String NAMES;
    private String PRICES;
    private String COLORS;
    private String DESCS;
    private String YEARS;
    private String CAR_STATUS;
    private String CAR_TYPES;
    private String MILEAGES;
    private String CAR_PHOTOS;
    private String DEALER_ID;
    private String CAR_ID;

    public Car(String NAMES, String PRICES, String COLORS, String DESCS, String YEARS, String CAR_STATUS, String CAR_TYPES, String MILEAGES, String CAR_PHOTOS, String DEALER_ID, String CAR_ID) {

        this.NAMES = NAMES;
        this.PRICES = PRICES;
        this.COLORS = COLORS;
        this.DESCS = DESCS;
        this.YEARS = YEARS;
        this.CAR_STATUS = CAR_STATUS;
        this.CAR_TYPES = CAR_TYPES;
        this.MILEAGES = MILEAGES;
        this.CAR_PHOTOS = CAR_PHOTOS;
        this.DEALER_ID = DEALER_ID;
        this.CAR_ID = CAR_ID;
    }

    public String getNAMES() {
        return NAMES;
    }

    public void setNAMES(String NAMES) {
        this.NAMES = NAMES;
    }

    public String getPRICES() {
        return PRICES;
    }

    public void setPRICES(String PRICES) {
        this.PRICES = PRICES;
    }

    public String getCOLORS() {
        return COLORS;
    }

    public void setCOLORS(String COLORS) {
        this.COLORS = COLORS;
    }

    public String getDESCS() {
        return DESCS;
    }

    public void setDESCS(String DESCS) {
        this.DESCS = DESCS;
    }

    public String getYEARS() {
        return YEARS;
    }

    public void setYEARS(String YEARS) {
        this.YEARS = YEARS;
    }

    public String getCAR_STATUS() {
        return CAR_STATUS;
    }

    public void setCAR_STATUS(String CAR_STATUS) {
        this.CAR_STATUS = CAR_STATUS;
    }

    public String getCAR_TYPES() {
        return CAR_TYPES;
    }

    public void setCAR_TYPES(String CAR_TYPES) {
        this.CAR_TYPES = CAR_TYPES;
    }

    public String getMILEAGES() {
        return MILEAGES;
    }

    public void setMILEAGES(String MILEAGES) {
        this.MILEAGES = MILEAGES;
    }

    public String getCAR_PHOTOS() {
        return CAR_PHOTOS;
    }

    public void setCAR_PHOTOS(String CAR_PHOTOS) {
        this.CAR_PHOTOS = CAR_PHOTOS;
    }

    public String getDEALER_ID() {
        return DEALER_ID;
    }

    public void setDEALER_ID(String DEALER_ID) {
        this.DEALER_ID = DEALER_ID;
    }

    public String getCAR_ID() {
        return CAR_ID;
    }

    public void setCAR_ID(String CAR_ID) {
        this.CAR_ID = CAR_ID;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
