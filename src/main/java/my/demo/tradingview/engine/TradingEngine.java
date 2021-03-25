package my.demo.tradingview.engine;

import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.repository.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TradingEngine implements Engines<OrderRequestDto> {

  private CacheRepository<OrderRequestDto> orderRepository;

  @Autowired
  @Override
  public void setRepository(CacheRepository<OrderRequestDto> repository) {
    this.orderRepository = repository;
  }

  @Override
  public void injectMessage(OrderRequestDto message) {

  }

  public void search(OrderRequestDto message) {

  }

}
