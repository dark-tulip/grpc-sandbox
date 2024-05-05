package kz.tansh;

import com.google.protobuf.InvalidProtocolBufferException;

import kz.tansh.proto.v14.v2.Type;
import kz.tansh.proto.v14.v3.Television;
import lombok.extern.slf4j.Slf4j;

/**
 * Переименование полей проходит без проблем, так как в десериализации участвуют числовые теги
 * НЕЛЬЗЯ изменять тип данных, потому что декодинг для них проходит по другому
 */

@Slf4j
public class L14_VersionsCompatibility {

  public static void main(String[] args) throws InvalidProtocolBufferException {
    var tv = kz.tansh.proto.v14.v2.Television.newBuilder()
                                             .setBrand("Test")
                                             .setModel(1999)
                                             .setType(Type.OLED)
                                             .setYear(333)  // у этого нового поля другой тэг
                                             .build();

    V1Parser.parse(tv.toByteArray());
    V2Parser.parse(tv.toByteArray());
    V3Parser.parse(tv.toByteArray());
  }
}


@Slf4j
class V1Parser {
  static void parse(byte[] bytes) throws InvalidProtocolBufferException {
    var tv = kz.tansh.proto.v14.v1.Television.parseFrom(bytes);
    log.info("brand: {}", tv.getBrand());
    log.info("year:  {}", tv.getYear());
    log.info("getUnknownFields: \n{}", tv.getUnknownFields());  // неизвестные поля тэг: значение
  }
}

@Slf4j
class V2Parser {
  static void parse(byte[] bytes) throws InvalidProtocolBufferException {
    var tv = kz.tansh.proto.v14.v2.Television.parseFrom(bytes);
    log.info("brand: {}", tv.getBrand());
    log.info("model: {}", tv.getModel());
    log.info("type:  {}", tv.getType());
    log.info("year:  {}", tv.getYear());
  }
}


@Slf4j
class V3Parser {
  static void parse(byte[] bytes) throws InvalidProtocolBufferException {
    var tv = Television.parseFrom(bytes);
    log.info("brand: {}", tv.getBrand());
    log.info("type:  {}", tv.getType());
  }
}
