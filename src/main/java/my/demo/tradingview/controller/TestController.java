package my.demo.tradingview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController {

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
    return orderService.delete(requestDto);
  }

}
