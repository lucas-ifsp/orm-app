package br.ifsp.example;

import br.ifsp.orm.AbstractDAO;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Car car = new Car("FXY8855", "Brazil", 2023);
        AbstractDAO<Car, String> carDao = new CarDAO();
        carDao.save(car);

        final Optional<Car> fxy8855 = carDao.findOne("FXY8855");
        fxy8855.ifPresent(System.out::println);

    }

}
