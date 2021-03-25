package my.demo.tradingview.controller;

import my.demo.tradingview.model.CacheableMessage;

public interface MessageController {

  <T> boolean broadcast(T message);

  <T extends CacheableMessage> boolean saveToCache(T message);

}
