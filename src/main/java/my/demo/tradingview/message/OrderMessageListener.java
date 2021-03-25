package my.demo.tradingview.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.config.websocket.SocketBinaryHandler;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.service.OrderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMessageListener {

  private final OrderService orderService;
  private final SocketBinaryHandler binaryHandler;

  @JmsListener(destination = "exchange.orders")
  public void queueOrders(OrderRequestDto ordersRequestDto) {
    log.info("queued :" + ordersRequestDto);

    boolean save = orderService.save(ordersRequestDto);
    if (!save) {
      return;
    }

    binaryHandler.broadcast(ordersRequestDto);
  }

}
