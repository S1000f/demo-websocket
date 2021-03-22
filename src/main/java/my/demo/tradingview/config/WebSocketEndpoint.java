package my.demo.tradingview.config;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/bin2")
public class WebSocketEndpoint {

  private static final Map<Session, String> sessionMap = new HashMap<>();

  private static void broadcast(ByteBuffer message) {
    sessionMap.keySet().forEach(s -> {
      try {
        s.getBasicRemote()
            .sendBinary(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("token") String token) {
    sessionMap.put(session, token);
  }

  @OnMessage
  public void onMessage(Session session, ByteBuffer message) {
    broadcast(message);
  }

  @OnClose
  public void onClose(Session session) {
  }

  @OnError
  public void onError(Session session) {
  }
}
