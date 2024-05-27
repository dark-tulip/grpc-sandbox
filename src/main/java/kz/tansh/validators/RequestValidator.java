package kz.tansh.validators;

import io.grpc.Status;

import java.util.Optional;

public class RequestValidator {
  public static Optional<Status> validateAccount(int accountNumber) {
    if (accountNumber > 0 && accountNumber < 10) {
      return Optional.empty();
    }
    return Optional.of(Status.INVALID_ARGUMENT.withDescription("Account number should be between 0 and 10"));
  }

  public static Optional<Status> isAmountDivisibleBy10(int amount) {
    if (amount > 0 && amount % 10 == 0) {
      return Optional.empty();
    }
    return Optional.of(Status.INVALID_ARGUMENT.withDescription("Amount should be divisible by 10"));
  }

  public static Optional<Status> hasEnoughBalance(int amount, int balance) {
    if (amount <= balance) {
      return Optional.empty();
    }
    return Optional.of(Status.INVALID_ARGUMENT.withDescription("Insufficient balance "));
  }
}
