package my.demo.tradingview.repository;

import java.util.List;

public interface CacheRepository<T, K> {

  boolean save(T message);

  boolean delete(T message);

  boolean deleteAll();

  List<T> findAll();

  T find(K key);
}
