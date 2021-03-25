package my.demo.tradingview.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.config.websocket.SocketBinaryHandler;
import my.demo.tradingview.engine.Engines;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.repository.CacheRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMessageRouter implements MessageRouter {

  private final ObjectMapper mapper = new ObjectMapper();
  private final SocketBinaryHandler socketBinaryHandler;
  private final CacheRepository<OrderRequestDto> orderRedisRepository;
  private final Engines<OrderRequestDto> tradingEngine;

  @Override
  public <T> boolean broadcast(T message) {
    String stringed;
    try {
      stringed = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
          .writeValueAsString(message);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return false;
    }

    socketBinaryHandler.broadcast(new BinaryMessage(stringed.getBytes(StandardCharsets.UTF_8)));

    return true;
  }

  @Override
  public <T> boolean saveToCache(T message) {
    OrderRequestDto orderMessage = (OrderRequestDto) message;
    return orderRedisRepository.save(orderMessage);
  }

  @Override
  public <T> boolean deleteFromCache(T message) {
    OrderRequestDto orderMessage = (OrderRequestDto) message;
    boolean delete = orderRedisRepository.delete(orderMessage);

    if (!delete) {
      return false;
    }

    orderMessage.setClose();
    return broadcast(orderMessage);
  }

  @Override
  public boolean clearCache() {
    return orderRedisRepository.deleteAll();
  }

  @Override
  public <T> void toEngine(T message) {
    tradingEngine.injectMessage((OrderRequestDto) message);
  }
}
