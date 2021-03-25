package my.demo.tradingview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController {

  private final ObjectMapper mapper = new ObjectMapper();
  private final SocketBinaryHandler socketBinaryHandler;
  private final OrderService orderService;

  @GetMapping("/test")
  public String test() {
    return "protocolbuf";
  }

  @ResponseBody
  @GetMapping("/clear")
  public boolean clearQueue() {
    return orderService.deleteAll();
  }

  @ResponseBody
  @PostMapping("/deal")
  public Boolean dealOrder(@RequestBody OrderRequestDto requestDto) {
    Boolean delete = orderService.delete(requestDto);

    if (!delete) {
      return false;
    }

    requestDto.setOrderType("CLOSE");
    String stringed;

    try {
      stringed = mapper.writeValueAsString(requestDto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return false;
    }

    socketBinaryHandler.broadcast(new BinaryMessage(stringed.getBytes(StandardCharsets.UTF_8)));

    return true;
  }

}
