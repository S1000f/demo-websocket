package my.demo.tradingview.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.controller.SocketBinaryHandler;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.service.OrderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMessageListener {

  private final ObjectMapper mapper = new ObjectMapper();
  private final OrderService orderService;
  private final SocketBinaryHandler binaryHandler;

  @JmsListener(destination = "exchange.orders")
  public void queueOrders(OrderRequestDto ordersRequestDto) {
    log.info("queued :" + ordersRequestDto);

    boolean save = orderService.save(ordersRequestDto);
    if (!save) {
      return;
    }

    BinaryMessage message;
    try {
      message = new BinaryMessage(mapper.writeValueAsBytes(ordersRequestDto));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return;
    }

    binaryHandler.broadcast(message);
  }

}
