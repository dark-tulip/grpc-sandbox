package kz.tansh.validators;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import kz.tansh.proto.v19.ErrorMessage;
import kz.tansh.proto.v19.ValidationStatusCode;

import java.util.Optional;

public class L19_RequestValidator {

  private static final Metadata.Key<ErrorMessage> ERROR_MESSAGE_KEY = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());

  /**
   * Status -> StatusRuntimeException
   */
  public static Optional<StatusRuntimeException> validateAccount(int accountNumber) {
    if (accountNumber > 0 && accountNumber < 10) {
      return Optional.empty();
    }
    var metadata = toMetadata(ValidationStatusCode.INVALID_ACCOUNT);

    // our custom metadata
    return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(metadata));
  }

  public static Optional<StatusRuntimeException> isAmountDivisibleBy10(int amount) {
    if (amount > 0 && amount % 10 == 0) {
      return Optional.empty();
    }
    var metadata = toMetadata(ValidationStatusCode.INVALID_AMOUNT);
    return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(metadata));
  }

  public static Optional<StatusRuntimeException> hasEnoughBalance(int amount, int balance) {
    if (amount <= balance) {
      return Optional.empty();
    }
    var metadata = toMetadata(ValidationStatusCode.INSUFFICIENT_BALANCE);
    return Optional.of(Status.FAILED_PRECONDITION.withDescription("Insufficient balance ").asRuntimeException(metadata));
  }


  private static Metadata toMetadata(ValidationStatusCode code) {
    var metadata = new Metadata();
    var value = ErrorMessage.newBuilder().setCode(code).setDescription("Something").build();
    metadata.put(ERROR_MESSAGE_KEY, value);
    return metadata;
  }

}
