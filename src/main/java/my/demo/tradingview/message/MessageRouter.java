package my.demo.tradingview.message;

public interface MessageRouter {

  <T> boolean broadcast(T message);

  <T> boolean saveToCache(T message);

  <T> boolean deleteFromCache(T message);

  boolean clearCache();

  <T> void toEngine(T message);

}
