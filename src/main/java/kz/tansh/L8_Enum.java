package kz.tansh;

import kz.tansh.proto.v8.Car;
import kz.tansh.proto.v8.Dealer;
import kz.tansh.proto.v8.Model;
import lombok.extern.slf4j.Slf4j;

/**
 * Пример использования мапы в формате
 * Map<Key, List<Object>>
 */
@Slf4j
public class L8_Enum {

  public static void main(String[] args) {
    Car car1 = Car.newBuilder()
                  .setModel(Model.BMW)
                  .setName("car 1")
                  .setYear(2000)
                  .build();

    Car car2 = Car.newBuilder()
                  .setModel(Model.MS)
                  .setName("car 2")
                  .setYear(0)
                  .build();


    System.out.println(car2);  // для экономии места, year не напечатается,
    // потому что ему задано нулевое значение,
    // но вы явно можете вывести значение с car.getYear()

    Dealer dealer = Dealer.newBuilder()
                          .putInventory(1234, car1)
                          .putInventory(123, car2)  // не покажет enum model, потому что нулевой тэг равен BMW
                          .build();

    log.info("\n{}", dealer);
  }
}
