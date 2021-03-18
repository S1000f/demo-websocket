package my.demo.tradingview.config;

import lombok.RequiredArgsConstructor;
import my.demo.tradingview.controller.BinaryHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@EnableWebSocket
@Configuration
public class WebSocketBinaryConfig implements WebSocketConfigurer {

  private final BinaryHandler binaryHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(binaryHandler, "/protobuf").setAllowedOrigins("*");
  }
}