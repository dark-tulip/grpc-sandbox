package kz.tansh;

import kz.tansh.proto.v10.Email;
import kz.tansh.proto.v10.Phone;
import kz.tansh.proto.v11.Authorization2;
import kz.tansh.proto.v11.Otp;
import lombok.extern.slf4j.Slf4j;

/**
 * Пример импорта других messages (классов) в прото файле
 */
@Slf4j
public class L11_ImportInProto {

  public static void main(String[] args) {
    Email email = Email.newBuilder()
                       .setEmail("test@gmail.com")
                       .setPassword("123456")
                       .build();

    Phone phone = Phone.newBuilder()
                       .setPhoneNumber("7776665544")
                       .setOtpCode("123")
                       .build();

    Otp otp = Otp.newBuilder()
                 .setCode("567")
                 .build();

    Authorization2 authorization = Authorization2.newBuilder()
                                                 .setPhone(phone)
                                                 .setEmail(email)
                                                 .setOtp(otp)
                                                 .build();

    // true
    log.info("{}", Authorization2.AuthTypeCase.OTP.equals(authorization.getAuthTypeCase()));
  }
}
