package kz.tansh;

import kz.tansh.proto.v6.Car;
import kz.tansh.proto.v6.Dealer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class L6_Map {

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

    Dealer dealer = Dealer.newBuilder()
                          .putInventory(car1.getYear(), car1)
                          .putInventory(car1.getYear(), car1)
                          .putInventory(car2.getYear(), car2)
                          .build();

    log.info("\n{}", dealer);
    log.info("\n{}", dealer.getInventoryCount());  // 2
    log.info("\n{}", dealer.containsInventory(19099));    // false
    log.info("\n{}", dealer.getInventoryOrThrow(19099));  // Exception java.lang.IllegalArgumentException
  }
}
