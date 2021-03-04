package my.demo.tradingview.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import my.demo.tradingview.model.ChartCandle;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {

  private final SimpMessagingTemplate template;

  public ChartController(SimpMessagingTemplate simpMessagingTemplate) {
    this.template = simpMessagingTemplate;
  }

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @Scheduled(initialDelay = 500, fixedRate = 200)
  public void sendCandle() {
    ChartCandle chartCandle = ChartCandle.builder()
        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .open(BigDecimal.valueOf(10.00))
        .high(BigDecimal.valueOf(19.45))
        .low(BigDecimal.valueOf(14.33))
        .close(BigDecimal.valueOf(16.55))
        .build();

    template.convertAndSend("/subscribe/candle", chartCandle);
  }

}
