package my.demo.tradingview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.message.MessageRouter;
import my.demo.tradingview.model.OrderRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RestController {

  private final MessageRouter messageRouter;

  @GetMapping("/test")
  public String test() {
    return "protocolbuf";
  }

  @ResponseBody
  @GetMapping("/clear")
  public boolean clearQueue() {
    return messageRouter.clearCache();
  }

  @ResponseBody
  @PostMapping("/deal")
  public boolean dealOrder(@RequestBody OrderRequestDto requestDto) {
    boolean delete = messageRouter.deleteFromCache(requestDto);

    if (!delete) {
      return false;
    }

    requestDto.setClose();
    return messageRouter.broadcast(requestDto);
  }

}
