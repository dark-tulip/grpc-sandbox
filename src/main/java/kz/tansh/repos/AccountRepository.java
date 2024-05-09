package kz.tansh.repos;

import kz.tansh.proto.v15.GetAccountResponse;

import java.util.Collection;
import java.util.HashMap;

public class AccountRepository {

  private final static HashMap<Integer, GetAccountResponse> db = new HashMap<>() {{
    put(1, GetAccountResponse.newBuilder().setAccountNumber(1).setBalance(100).build());
    put(2, GetAccountResponse.newBuilder().setAccountNumber(2).setBalance(200).build());
    put(3, GetAccountResponse.newBuilder().setAccountNumber(3).setBalance(300).build());
    put(4, GetAccountResponse.newBuilder().setAccountNumber(4).setBalance(400).build());
  }};

  public GetAccountResponse getAccount(Integer accountNumber) {
    return db.get(accountNumber);
  }

  public Collection<GetAccountResponse> getAccounts() {
    return db.values();
  }

  public void minusBalance(Integer accountNumber, Integer amount) {
    db.computeIfPresent(accountNumber, (k, v) -> GetAccountResponse.newBuilder()
                                                                   .setAccountNumber(k)
                                                                   .setBalance(v.getBalance() - amount)
                                                                   .build());
  }

  public void addBalance(int accountNumber, int amount) {
    db.computeIfPresent(accountNumber, (k, v) -> GetAccountResponse.newBuilder().setAccountNumber(k)
                                                                   .setBalance(v.getBalance() + amount)
                                                                   .build());
  }
}
