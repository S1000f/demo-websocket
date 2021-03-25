package my.demo.tradingview.engine;

import my.demo.tradingview.repository.CacheRepository;

public interface Engines<T> {

  void injectMessage(T message);

  void setRepository(CacheRepository<T> repository);

}
