package kz.tansh.repos;

import kz.tansh.proto.v15.GetAccountResponse;

import java.util.Collection;
import java.util.HashMap;

public class AccountRepository {


  private final static HashMap<Integer, GetAccountResponse> bankAccountsRepository = new HashMap<>() {{
    put(1, GetAccountResponse.newBuilder().setAccountNumber(1).setBalance(100).build());
    put(2, GetAccountResponse.newBuilder().setAccountNumber(2).setBalance(200).build());
    put(3, GetAccountResponse.newBuilder().setAccountNumber(3).setBalance(300).build());
    put(4, GetAccountResponse.newBuilder().setAccountNumber(4).setBalance(400).build());
  }};

  public GetAccountResponse getAccount(Integer accountNumber) {
    return bankAccountsRepository.get(accountNumber);
  }

  public Collection<GetAccountResponse> getAccounts() {
    return bankAccountsRepository.values();
  }

  public void withdraw(Integer accountNumber, Integer amount) {
    bankAccountsRepository.computeIfPresent(accountNumber, (k, v) -> GetAccountResponse.newBuilder()
                                                                                       .setAccountNumber(k)
                                                                                       .setBalance(v.getBalance() - amount)
                                                                                       .build());
  }
}
