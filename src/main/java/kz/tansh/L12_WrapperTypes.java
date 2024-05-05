package kz.tansh;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Timestamp;
import kz.tansh.proto.v12.Sample;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * Пример импорта других messages (классов) в прото файле
 */
@Slf4j
public class L12_WrapperTypes {

  public static void main(String[] args) {
    Sample sample = Sample.newBuilder().setAge(Int32Value.of(33))
                          .setAge2(44)
                          .setTimestamp(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
                          .build();

    // Типы обертки имеют объектные методы как hasSomething(), hashCode()
    log.info("has age: {}", sample.hasAge());
    log.info("timestamp: {}", Instant.ofEpochSecond(sample.getTimestamp().getSeconds()));  // 2024-05-01T19:49:11Z

    log.info("getAge2 (primitive): {}", sample.getAge2());
    log.info("getAge (wrapper): {}", sample.getAge());

    log.info("sample:\n{} has age: ", sample);

  }
}
