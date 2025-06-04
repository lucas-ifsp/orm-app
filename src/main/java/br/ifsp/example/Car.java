package br.ifsp.example;

import br.ifsp.orm.Entity;

public class Car implements Entity {
    private String plate;
    private String brand;
    private int year;

    public Car() {
    }

    public Car(String plate, String brand, int year) {
        this.plate = plate;
        this.brand = brand;
        this.year = year;
    }

    @Override
    public String idFieldName() {
        return "plate";
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Car{" +
               "plate='" + plate + '\'' +
               ", brand='" + brand + '\'' +
               ", year=" + year +
               '}';
    }
}
