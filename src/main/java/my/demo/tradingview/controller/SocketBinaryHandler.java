package my.demo.tradingview.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.lib.SecurityUtils;
import my.demo.tradingview.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketBinaryHandler extends BinaryWebSocketHandler {

  private final Set<WebSocketSession> broadcast = new HashSet<>();
  private final Map<String, WebSocketSession> sessionMap = new HashMap<>();
  private final Map<String, String> tokenSessionIdMap = new HashMap<>();
  private final OrderService orderService;

  public void broadcast(BinaryMessage message) {
    broadcast.forEach(session -> handleBinaryMessage(session, message));
  }

  public boolean sendToUser(String token, BinaryMessage message) {
    String sessionId = tokenSessionIdMap.get(token);
    if (sessionId != null && !sessionId.isEmpty()) {
      handleBinaryMessage(sessionMap.get(sessionId), message);
      return true;
    }

    return false;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    orderService.getBinMessageList()
        .forEach(m -> handleBinaryMessage(session, m));

    broadcast.add(session);
    sessionMap.put(session.getId(), session);
  }

  @Override
  protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    if (SecurityUtils.hasToken(message)) {
      String token = SecurityUtils.extractToken(message);
      if (token != null) {
        tokenSessionIdMap.put(token, session.getId());
        return;
      }
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
    String sessionId = session.getId();

    broadcast.remove(session);
    sessionMap.remove(sessionId);
    tokenSessionIdMap.values()
        .remove(sessionId);
  }

}
