package kz.tansh;

import com.google.protobuf.Int32Value;
import kz.tansh.proto.v13.SampleV2;
import lombok.extern.slf4j.Slf4j;

/**
 * Пример разницы опциональных типов и классов оберток
 */
@Slf4j
public class L13_OptionalType {

  public static void main(String[] args) {
    SampleV2 sample = SampleV2.newBuilder()
                              .setAge2(22)
                              .setAge(Int32Value.of(33))
                              .build();

    // Типы обертки имеют объектные методы как hasSomething(), hashCode()
    log.info("wrapper age: {}", sample.hasAge());    // true
    log.info("optional age: {}", sample.hasAge2());  // true
    log.info("age wrapper: {}", sample.getAge());    // output: value: 33
    log.info("age optional: {}", sample.getAge2());  // output: 22

    //    age {
    //      value: 33
    //    }
    //    age2: 22
    log.info("sample:\n{}", sample);

  }
}
