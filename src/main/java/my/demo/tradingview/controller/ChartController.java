package my.demo.tradingview.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import my.demo.tradingview.model.ChartCandle;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ChartController {

  private final SimpMessagingTemplate template;

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @Scheduled(initialDelay = 500, fixedRate = 200)
  public void sendCandle() {
    Random random = new Random();
    int randomOpen = random.nextInt(10) + 1;
    int randomClose = random.nextInt(randomOpen + 10) + 1;

    ChartCandle chartCandle = ChartCandle.builder()
        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .open(BigDecimal.valueOf(randomOpen))
        .high(BigDecimal.valueOf(19.45))
        .low(BigDecimal.valueOf(14.33))
        .close(BigDecimal.valueOf(randomClose))
        .build();

    template.convertAndSend("/subscribe/candle", chartCandle);
  }

}
