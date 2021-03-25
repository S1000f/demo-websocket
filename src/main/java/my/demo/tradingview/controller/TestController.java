package my.demo.tradingview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.config.websocket.SocketBinaryHandler;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.service.OrderCacheService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController implements MessageController {

  private final ObjectMapper mapper = new ObjectMapper();
  private final SocketBinaryHandler socketBinaryHandler;
  private final OrderCacheService orderCacheService;

  @GetMapping("/test")
  public String test() {
    return "protocolbuf";
  }

  @ResponseBody
  @GetMapping("/clear")
  public boolean clearQueue() {
    return orderCacheService.deleteAll();
  }

  @ResponseBody
  @PostMapping("/deal")
  public boolean dealOrder(@RequestBody OrderRequestDto requestDto) {
    boolean delete = orderCacheService.delete(requestDto);

    if (!delete) {
      return false;
    }

    requestDto.setClose();
    return broadcast(requestDto);
  }

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
    OrderRequestDto message1 = (OrderRequestDto) message;
    return orderCacheService.save(message1);
  }
}
