package my.demo.tradingview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController {

  private final SocketBinaryHandler socketBinaryHandler;

  @GetMapping("/test")
  public String test() {
    return "protocolbuf";
  }

}
