package my.demo.tradingview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.model.OrderRequestDto;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Slf4j
@Component
public class SocketBinaryHandler extends BinaryWebSocketHandler {

  private final ObjectMapper mapper = new ObjectMapper();
  private final List<WebSocketSession> socketSessions = new ArrayList<>();
  private final Map<WebSocketSession, String> socketSessionMap = new HashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    socketSessions.add(session);
  }

  @JmsListener(destination = "exchange.orders")
  public void queueOrders(OrderRequestDto ordersRequestDto) throws Exception {
    log.info("queued :" + ordersRequestDto);

    BinaryMessage message = new BinaryMessage(mapper.writeValueAsBytes(ordersRequestDto));
    socketSessions.forEach(session -> handleBinaryMessage(session, message));
  }

  @Override
  protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    if (!socketSessionMap.containsKey(session) && message.getPayloadLength() == 8) {
      log.info("input data is 8 bytes");
      String token = StandardCharsets.UTF_8.decode(message.getPayload()).toString();
      socketSessionMap.put(session, token);

    }

    try {
      session.sendMessage(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    super.handleTransportError(session, exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    socketSessions.remove(session);
  }

}
