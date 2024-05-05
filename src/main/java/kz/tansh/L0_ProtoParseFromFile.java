package kz.tansh;

import kz.tansh.proto.v3.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class L0_ProtoParseFromFile {
  public static final Path path = Path.of("person_out.txt");

  public static void main(String[] args) throws IOException {
    var person1 = Person.newBuilder()
                        .setAge(12)
                        .setEmployed(true)
                        .setEmail("test@mail.ru")
                        .setBalance(-10_000)
                        .setBankAccountNumber(123213123L)
                        .setSalary(10000.00)
                        .build();

    serialize(person1);
    log.info("is equals: " + person1.equals(deserialize()));  // true
  }

  public static void serialize(Person person) throws IOException {
    try (var fos = Files.newOutputStream(path)) {
      person.writeTo(fos);
    }
  }

  public static Person deserialize() throws IOException {
    try (var fis = Files.newInputStream(path)) {
      return Person.parseFrom(fis);
    }
  }

}
