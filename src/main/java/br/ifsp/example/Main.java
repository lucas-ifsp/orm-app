package br.ifsp.example;

import com.github.javafaker.Faker;

import java.util.Optional;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        Faker faker = Faker.instance();
        Random random = new Random();

        int randomInt = random.nextInt(1000, 9999);
        String plate = "ORM" + randomInt;
        int year = random.nextInt(2000, 2025);
        String country = faker.country().name();

        Car car = new Car(plate, country, year);

        CarDAO carDao = new CarDAO();
        carDao.save(car);

        System.out.println("Saved car with plate: " + plate);
        final Optional<Car> obtainedCar = carDao.findOne(plate);
        obtainedCar.ifPresent(System.out::println);

        System.out.println("\nSaved cars: ");
        carDao.findAll().forEach(System.out::println);

        car.setYear(year - 5);
        carDao.update(car);

        System.out.println("\nUpdated car year with plate: " + plate);
        final Optional<Car> updatedCar = carDao.findOne(plate);
        updatedCar.ifPresent(System.out::println);

        // Comment this section this to save more cars into database
        System.out.println("\nCars after deleting car with plate: " + plate);
        carDao.deleteByKey(plate);
        carDao.findAll().forEach(System.out::println);

        final String name = faker.food().sushi();
        double randomPrice = 15 + (40 - 15) * random.nextDouble();
        int randomQuantity = random.nextInt(2, 12);

        Sushi sushi = new Sushi(name, randomPrice, randomQuantity);

        SushiDAO sushiDao = new SushiDAO();
        sushiDao.save(sushi);

        System.out.println("\nSaved sushi with name: " + name);
        final Optional<Sushi> obtainedSushi = sushiDao.findOne(name);
        obtainedSushi.ifPresent(System.out::println);

        System.out.println("\nSaved sushis: ");
        sushiDao.findAll().forEach(System.out::println);

        sushi.setQuantity(randomQuantity + 10);
        sushiDao.update(sushi);

        System.out.println("\nUpdated sushi quantity with name: " + name);
        final Optional<Sushi> updatedSushi = sushiDao.findOne(name);
        updatedSushi.ifPresent(System.out::println);

        // Comment this section this to save more sushis into database
        System.out.println("\nSushis after deleting sushi with name: " + name);
        sushiDao.deleteByKey(name);
        sushiDao.findAll().forEach(System.out::println);
    }

}
