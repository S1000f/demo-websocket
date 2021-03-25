package my.demo.tradingview.config;

import lombok.RequiredArgsConstructor;
import my.demo.tradingview.controller.SocketBinaryHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@EnableWebSocket
@Configuration
public class WebSocketBinaryConfig implements WebSocketConfigurer {

  private final SocketBinaryHandler socketBinaryHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(socketBinaryHandler, "/bin")
        .setAllowedOrigins("http://192.168.0.93:8080", "http://192.168.0.36:8080");
  }
}