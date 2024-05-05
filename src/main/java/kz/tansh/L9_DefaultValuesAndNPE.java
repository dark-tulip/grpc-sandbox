package kz.tansh;

import kz.tansh.proto.v3.Person;
import kz.tansh.proto.v4.Address;
import kz.tansh.proto.v4.School;
import kz.tansh.proto.v5.Library;
import kz.tansh.proto.v8.Car;
import kz.tansh.proto.v8.Dealer;
import kz.tansh.proto.v8.Model;
import lombok.extern.slf4j.Slf4j;

/**
 * Особенности работы с преинициализированными объектами
 */
@Slf4j
public class L9_DefaultValuesAndNPE {

  public static void main(String[] args) {
    Car car1 = Car.newBuilder().build();
    Car car2 = Car.newBuilder().getDefaultInstanceForType();
    log.info("{}", car1.equals(car2));          // true, getDefaultInstanceForType модель по умолчанию
    log.info("getModel: {}", car1.getModel());  // bmw, enum с кодом 0 всегда стоит как default type
    log.info("getYear: {}", car1.getYear());    // 0, примитивный тип имеет значение 0
    log.info("getName: {}", car1.getName());    // empty пустая строка

    Dealer dealer = Dealer.newBuilder().build();
    log.info("getInventoryMap: {}", dealer.getInventoryMap());  // пустое множество {}

    Library library = Library.getDefaultInstance();
    log.info("getBooksList {}: ", library.getBooksList());

    School school = School.getDefaultInstance();
    log.info("getAddress: {}", school.getAddress());                        // empty пустая строка
    log.info("getAddress().getCity(): {}", school.getAddress().getCity());  // empty пустая строка, тут не кинет NPE потому что есть объект по умолчанию "Адрес"
    log.info("hasAddress(): {}", school.hasAddress());                      // false Адреса нет, но есть объект по умолчанию
    log.info("getDefaultInstance equals to school.getAddress(): {}", Address.getDefaultInstance().equals(school.getAddress()));  // true

    Person person = Person.getDefaultInstance();
    log.info("is employed: {}", person.getEmployed());

  }
}
