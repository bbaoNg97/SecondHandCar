package my.edu.tarc.secondhandcar;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by bbao_Ng on 20/11/2018.
 */

public class Car {

    private String NAMES;
    private int PRICES;
    private String COLORS;
    private String DESCS;
    private int YEARS;
    private String CAR_STATUS;
    private String CAR_TYPES;
    private int MILEAGES;
    private String CAR_PHOTOS;
    private String DEALER_ID;
    private String CAR_ID;

    public Car(String NAMES, int PRICES, String COLORS, String DESCS, int YEARS, String CAR_STATUS, String CAR_TYPES, int MILEAGES, String CAR_PHOTOS, String DEALER_ID, String CAR_ID) {

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

    public int getPRICES() {
        return PRICES;
    }

    public void setPRICES(int PRICES) {
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

    public int getYEARS() {
        return YEARS;
    }

    public void setYEARS(int YEARS) {
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

    public int getMILEAGES() {
        return MILEAGES;
    }

    public void setMILEAGES(int MILEAGES) {
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

   /* public int compareTo(@NonNull Car compareCar) {

    }*/

    /*Comparator for sorting the list by Price acsending*/

    public static Comparator<Car> PriceComparator= new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            int carPrice1 = c1.getPRICES();
            int carPrice2 = c2.getPRICES();
            return carPrice1 - carPrice2;
        }

    };
/*Comparator for sorting the list by Mileage ascending*/

    public static Comparator<Car> MileageComparator = new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            int carMileage1 = c1.getMILEAGES();
            int carMileage2 = c2.getMILEAGES();
            return carMileage1 - carMileage2;
        }

    };
/*Comparator for sorting the list by Year ascending*/

    public static Comparator<Car> YearComparator = new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            int carYear1 = c1.getYEARS();
            int carYear2 = c2.getYEARS();
            return carYear1 - carYear2;
        }

    };


}
