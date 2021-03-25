package my.demo.tradingview.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // CORS 설정시 '*' 와일드카드 사용불가
    registry.addEndpoint("/chart")
        .setAllowedOriginPatterns("http://192.168.0**")
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 클라이언트 -> 서버가 메시지를 받을 때
    registry.setApplicationDestinationPrefixes("/publish");
    // 서버 -> 클라이언트 메시지를 내려줄 때
    registry.enableSimpleBroker("/subscribe");
  }

}
