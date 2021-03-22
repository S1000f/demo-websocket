package my.demo.tradingview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import my.demo.tradingview.message.CandleProto;
import my.demo.tradingview.message.CandleProto.Candle;
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

  @GetMapping("/chart")
  public String chart() {
    return "chart";
  }

  @GetMapping("/protobuf")
  public String protobuf() {
    return "protocolbuf";
  }

  @GetMapping("/base64")
  public String base64() {
    return "base64";
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

    template.convertAndSend("/bin", chartCandle);
  }

  @Scheduled(initialDelay = 500, fixedRate = 200)
  public void sendBase64() throws JsonProcessingException {
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

    ObjectMapper mapper = new ObjectMapper();
    Base64.Encoder encoder = Base64.getEncoder();
    byte[] encode = encoder.encode(mapper.writeValueAsBytes(chartCandle));

    template.convertAndSend("/subscribe/base64", Arrays.toString(encode));
  }

  @Scheduled(initialDelay = 500, fixedRate = 200)
  public void sendProtobuf() {
    Random random = new Random();
    int randomOpen = random.nextInt(10) + 1;
    int randomClose = random.nextInt(randomOpen + 10) + 1;

    CandleProto.Candle candle = Candle.newBuilder()
      .setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
      .setOpen(String.valueOf(randomOpen))
      .setHigh(String.valueOf(19.45))
      .setLow(String.valueOf(14.33))
      .setClose(String.valueOf(randomClose))
      .build();

    template.convertAndSend("/subscribe/protobuf", candle.toByteString());
  }

}
