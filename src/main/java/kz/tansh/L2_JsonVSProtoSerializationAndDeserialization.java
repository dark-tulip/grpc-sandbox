package kz.tansh;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import kz.tansh.proto.v3.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class L2_JsonVSProtoSerializationAndDeserialization {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Gson gson = new Gson();

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

    // Выигрыш в скорости при частой десериализации больших объемов данных
    //    14:59:43.749 INFO : Proto run in 1305 msec
    //    14:59:49.606 INFO : Json run in 5856 msec
    //    14:59:50.670 INFO : Proto run in 1064 msec
    //    14:59:56.427 INFO : Json run in 5757 msec
    //    14:59:57.512 INFO : Proto run in 1084 msec
    //    15:00:03.224 INFO : Json run in 5712 msec
    //    15:00:04.336 INFO : Proto run in 1111 msec
    //    15:00:10.192 INFO : Json run in 5856 msec
    //    15:00:11.379 INFO : Proto run in 1186 msec
    //    15:00:17.091 INFO : Json run in 5711 msec
    for (int i = 0; i < 5; i++) {
      runTest("Proto", () -> proto(protoPerson));
      runTest("Json", () -> json(jsonPerson));
      runTest("Json (gson)", () -> json(jsonPerson));
    }

  }

  /**
   * Сериализовать и десериализовать объект
   */
  private static void proto(Person person) {
    try {
      byte[] bytes = person.toByteArray();
      Person.parseFrom(bytes);
    } catch (InvalidProtocolBufferException e) {
      throw new RuntimeException(e);
    }
  }


  private static void json(JsonPerson jsonPerson) {
    try {
      byte[] bytes = objectMapper.writeValueAsBytes(jsonPerson);
      objectMapper.readValue(bytes, JsonPerson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void jsonGson(JsonPerson jsonPerson) {
    byte[] bytes = gson.toJson(jsonPerson).getBytes();
    log.info("Json bytes: " + bytes.length);
    gson.fromJson(new String(bytes), JsonPerson.class);
  }

  private static void runTest(String testName, Runnable r) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10_000_000; i++) {
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
