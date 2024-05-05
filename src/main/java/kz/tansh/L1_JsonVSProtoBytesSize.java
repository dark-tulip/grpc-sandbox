package kz.tansh;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import kz.tansh.proto.v3.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class L1_JsonVSProtoBytesSize {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    var protoPerson = Person.newBuilder()
                            .setLastName("test")
                            .setAge(12)
                            .setEmployed(true)
                            .setEmail("test@mail.ru")
                            .setBalance(-10_000)
                            .setBankAccountNumber(123213123L)
                            .setSalary(10000.00)
                            .build();

    var jsonPerson = new JsonPerson(
        "test",
        12,
        "test@mail.ru",
        true,
        10000.00,
        123213123L,
        -10_000
    );

    // Одинаковые свойства объекта занимают разный объем в байтах
    // Proto bytes: 42
    // Json bytes: 131
    runTest("Proto", () -> proto(protoPerson));
    runTest("Json", () -> json(jsonPerson));
  }

  /**
   * Сериализовать и десериализовать объект
   */
  private static void proto(Person person) {
    try {
      byte[] bytes = person.toByteArray();
      log.info("Proto bytes: " + bytes.length);
      Person.parseFrom(bytes);
    } catch (InvalidProtocolBufferException e) {
      throw new RuntimeException(e);
    }
  }


  private static void json(JsonPerson jsonPerson) {
    try {
      byte[] bytes = objectMapper.writeValueAsBytes(jsonPerson);
      log.info("Json bytes: " + bytes.length);
      objectMapper.readValue(bytes, JsonPerson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void runTest(String testName, Runnable r) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      r.run();
    }
    long end = System.currentTimeMillis();
    log.info("{} run in {} msec", testName, end - start);
  }

  private record JsonPerson(
      String lastName,
      int age,
      String email,
      boolean employed,
      double salary,
      long bankAccountNumber,
      int balance
  ) {
  }
}
