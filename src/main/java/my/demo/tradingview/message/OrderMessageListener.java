package my.demo.tradingview.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.controller.MessageController;
import my.demo.tradingview.model.OrderRequestDto;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMessageListener {

  private final MessageController messageController;

  @JmsListener(destination = "exchange.orders")
  public void queueOrders(OrderRequestDto ordersRequestDto) {
    log.info("queued :" + ordersRequestDto);

    boolean result = messageController.saveToCache(ordersRequestDto);
    if (!result) {
      return;
    }

    messageController.broadcast(ordersRequestDto);
  }

}
