package kz.tansh;

import kz.tansh.proto.v7.Car;
import kz.tansh.proto.v7.Cars;
import kz.tansh.proto.v7.Dealer;
import lombok.extern.slf4j.Slf4j;

/**
 * Пример использования мапы в формате
 * Map<Key, List<Object>>
 */
@Slf4j
public class L7_ComplexMap {

  public static void main(String[] args) {
    Car car1 = Car.newBuilder()
                  .setName("car 1")
                  .setModel("bmw")
                  .setYear(2000)
                  .build();

    Car car2 = Car.newBuilder()
                  .setName("car 2")
                  .setModel("ms")
                  .setYear(2002)
                  .build();

    Cars cars = Cars.newBuilder()
        .addCars(car1)
        .addCars(car2)
        .build();

    Dealer dealer = Dealer.newBuilder()
                          .putInventory(1234, cars)
                          .build();

    log.info("\n{}", dealer);
  }
}
