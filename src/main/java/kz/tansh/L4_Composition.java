package kz.tansh;

import kz.tansh.proto.v4.Address;
import kz.tansh.proto.v4.School;
import kz.tansh.proto.v4.Student;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class L4_Composition {

  public static void main(String[] args) {
    Address address = Address.newBuilder()
        .setCity("Almaty")
        .setStreet("Abay")
        .setState("Almaly")
        .build();

    School school = School.newBuilder()
        .setAddress(address)
        .setName("School #18")
        .build();

    Student student = Student.newBuilder()
        .setName("Student Studentovich")
        .setAddress(address.toBuilder().setCity("Astana"))
        .setSchool(school)
        .build();

    // buildPartial - это легаси код с proto2 файлов, который имел опциональные поля
    // когда вы использовали билд и не заполняли обязательные поля - кидалось исключение, но buildPartial не кидал
    log.info("Student: \n" + student);

  }
}
