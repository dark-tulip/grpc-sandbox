package kz.tansh.services;

public class RunLocalServer {

  public static void main(String[] args) {
    GrpcServer.create(6565, new BankService())
              .start()
              .await();
  }
}
