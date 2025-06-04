package br.ifsp.example;

import br.ifsp.orm.AbstractDAO;
import br.ifsp.orm.SQLBuilder;
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

        System.out.println(SQLBuilder.save(car.getClass()));
        System.out.println(SQLBuilder.update(car.getClass(), "plate"));

//        CarDAO carDao = new CarDAO();
//        carDao.save(car);
//
//        final Optional<Car> obtainedCar = carDao.findOne(plate);
//        obtainedCar.ifPresent(System.out::println);
//
//        final String name = faker.food().sushi();
//        double randomPrice = 15 + (40 - 15) * random.nextDouble();
//        int randomQuantity = random.nextInt(2, 12);
//
//        Sushi sushi = new Sushi(name, randomPrice, randomQuantity);
//        SushiDAO sushiDao = new SushiDAO();
//        sushiDao.save(sushi);
//
//        final Optional<Sushi> obtainedSushi = sushiDao.findOne(name);
//        obtainedSushi.ifPresent(System.out::println);
    }

}
