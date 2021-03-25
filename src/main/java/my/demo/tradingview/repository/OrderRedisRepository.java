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
public class OrderRedisRepository implements CacheRepository<OrderRequestDto> {

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
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

  @Override
  public boolean delete(OrderRequestDto requestDto) {
    if (requestDto == null) {
      return false;
    }

    String redisKey;
    try {
      redisKey = SecurityUtils.buildRedisKey(requestDto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return false;
    }

    Boolean delete = redisTemplate.delete("token:" + redisKey);

    return delete != null && delete;
  }

  @Override
  public boolean deleteAll() {
    Set<String> keys = redisTemplate.keys("token*");

    if (keys == null || keys.isEmpty()) {
      return false;
    }
    redisTemplate.delete(keys);

    return true;
  }

  @Override
  public List<OrderRequestDto> findAll() {
    Set<String> keys = redisTemplate.keys("token*");

    if (keys == null || keys.isEmpty()) {
      return Collections.emptyList();
    }

    List<String> strings = redisTemplate.opsForValue()
        .multiGet(keys);

    if (strings == null || strings.isEmpty()) {
      return Collections.emptyList();
    }

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
