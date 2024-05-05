package kz.tansh;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import kz.tansh.models.JsonPerson;
import kz.tansh.proto.v3.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class L3_ProtoSerializationOfBoolean {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    boolean isEmployed = false;
    var protoPerson = Person.newBuilder()
                            .setEmployed(isEmployed)
                            .build();

    var jsonPerson = new JsonPerson();
    jsonPerson.setEmployed(isEmployed);

    //    Сериализация для булевых значений, true
    //    15:54:16.686 INFO : Proto bytes: 2
    //    15:54:16.710 INFO : Json bytes: 101
    //    Сериализация для булевых значений, false
    //    15:54:39.894 INFO : Proto bytes: 0
    //    15:54:39.910 INFO : Json bytes: 102
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
    r.run();
  }
}

