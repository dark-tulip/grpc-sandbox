package kz.tansh;

import kz.tansh.proto.v10.Authorization;
import kz.tansh.proto.v10.Email;
import kz.tansh.proto.v10.Phone;
import lombok.extern.slf4j.Slf4j;

/**
 * Результатом one of является обычный enum
 * где значение по умолчанию это AUTHTYPE_NOT_SET (с тэгом 0)
 * Если в сеттер присвоили оба значения, берется последний продекларированный
 */
@Slf4j
public class L10_OneOf {

  public static void main(String[] args) {
    Email email = Email.newBuilder()
                       .setEmail("test@gmail.com")
                       .setPassword("123456")
                       .build();

    Phone phone = Phone.newBuilder()
                       .setPhoneNumber("7776665544")
                       .setOtpCode("123")
                       .build();

    Authorization authorization = Authorization.newBuilder()
                                               .setPhone(phone)
                                               .build();

    // class kz.tansh.proto.v10.Authorization$AuthTypeCase
    log.info("{}", authorization.getAuthTypeCase().getDeclaringClass());
    log.info("{}", authorization.getAuthTypeCase().getNumber());  // 2

    authorization = Authorization.getDefaultInstance();
    log.info("{}", authorization.getAuthTypeCase().getNumber());  // 0

    // добавляется новое поле typeCase + default value for enum
    String res = switch (authorization.getAuthTypeCase()) {
      case PHONE -> "Instance is phone";
      case EMAIL -> "Instance is email";
      case AUTHTYPE_NOT_SET -> "Auth type not set";
    };

    log.info("result: {}", res);

    // Если в сеттер присвоили оба значения, берется последний продекларированный
    Authorization authorization2 = Authorization.newBuilder()
                                                .setPhone(phone)
                                                .setEmail(email)
                                                .build();

    log.info("auth email: {}", authorization2.getEmail());
    log.info("auth phone: {}", authorization2.getPhone());  // empty, даже если сам объект phone не пустой


  }
}
