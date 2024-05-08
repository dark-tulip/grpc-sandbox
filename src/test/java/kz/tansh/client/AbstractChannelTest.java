package kz.tansh.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.TimeUnit;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractChannelTest {

  protected ManagedChannel channel;

  @BeforeAll
  public void setUpChannel() {
    this.channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                                   .usePlaintext()
                                   .build();
  }

  @AfterAll
  void stopChannel() throws InterruptedException {
    this.channel.shutdown()
                .awaitTermination(5, TimeUnit.SECONDS);
  }
}
