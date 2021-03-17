package my.demo.tradingview.message;

import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMessageListener {

  private final SimpMessagingTemplate messagingTemplate;

  @JmsListener(destination = "exchange.orders")
  public void queueOrders(OrdersRequestDto ordersRequestDto) {
    log.info("queued :" + ordersRequestDto);

    messagingTemplate.convertAndSend("/subscribe/orders", ordersRequestDto);
  }

  @Data
  public static class OrdersRequestDto {

    private String marketPair;
    private Boolean isBuy;
    private BigDecimal price;
    private BigDecimal amount;
    private String orderType;
  }

}
