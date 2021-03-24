package my.demo.tradingview.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.lib.SecurityUtils;
import my.demo.tradingview.model.OrderRequestDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class OrderRedisRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper mapper = new ObjectMapper();

  public boolean save(OrderRequestDto requestDto) {
    String stringed;
    try {
      stringed = mapper.writeValueAsString(requestDto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return false;
    }

    String hashed = SecurityUtils.getSha3bit256(stringed);

    redisTemplate.opsForValue()
        .set("token:" + hashed, stringed);

    return true;
  }

  public Boolean delete(String redisKey) {
    return redisTemplate.delete("token:" + redisKey);
  }

  public boolean deleteAll() {
    Set<String> keys = redisTemplate.keys("token*");

    if (keys == null || keys.isEmpty()) {
      return false;
    }
    redisTemplate.delete(keys);

    return true;
  }

  public List<OrderRequestDto> findAll() {

    log.info("redisRepo findAll enter...");

    Set<String> keys = redisTemplate.keys("token*");

    if (keys == null || keys.isEmpty()) {
      return Collections.emptyList();
    }

    List<String> strings = redisTemplate.opsForValue()
        .multiGet(keys);

    if (strings == null || strings.isEmpty()) {
      return Collections.emptyList();
    }

    log.info("redis repo find message list: " + strings);

    return strings.stream()
        .map(s -> {
          try {
            return mapper.readValue(s, OrderRequestDto.class);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new OrderRequestDto();
          }
        })
        .collect(Collectors.toList());
  }

}
