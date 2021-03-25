package my.demo.tradingview.engine;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TradingEngineTest {

  @Autowired
  private TradingEngine tradingEngine;

  @Test
  public void test() {
    log.info("engine init: " + tradingEngine.getOrderRepository().findAll());
  }

}
