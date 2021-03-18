package my.demo.tradingview.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import my.demo.tradingview.message.CandleProto;
import my.demo.tradingview.message.CandleProto.Candle;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Component
public class BinaryHandler extends BinaryWebSocketHandler {

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
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

    session.sendMessage(new TextMessage(candle.toByteArray()));
  }

}
