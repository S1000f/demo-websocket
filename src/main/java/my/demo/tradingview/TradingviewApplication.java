package my.demo.tradingview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TradingviewApplication {

  public static void main(String[] args) {
    SpringApplication.run(TradingviewApplication.class, args);
  }

}
