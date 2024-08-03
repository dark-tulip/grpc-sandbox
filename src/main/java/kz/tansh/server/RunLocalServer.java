package kz.tansh.server;

import kz.tansh.*;

public class RunLocalServer {

  public static void main(String[] args) {
    GrpcServer.create(6565,
                  new L15_BankService(),
                  new L18_BankService(),
                  new L15_TransferService(),
                  new L16_FlowControlService(),
                  new L17_GuessGameService(),
                  new L19_TrailersMetadataBankService()
              )
              .start()
              .await();
  }
}
