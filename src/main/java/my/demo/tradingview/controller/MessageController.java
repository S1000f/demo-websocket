package my.demo.tradingview.controller;

public interface MessageController {

  <T> boolean broadcast(T message);

  <T> boolean saveToCache(T message);

}
