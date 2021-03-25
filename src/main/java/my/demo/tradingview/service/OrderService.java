package my.demo.tradingview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.model.OrderRequestDto;
import my.demo.tradingview.repository.OrderRedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

  private final ObjectMapper mapper = new ObjectMapper();
  private final OrderRedisRepository redisRepository;

  public boolean save(OrderRequestDto requestDto) {
    return redisRepository.save(requestDto);
  }

  public Boolean delete(OrderRequestDto orderRequestDto) {
    return redisRepository.delete(orderRequestDto);
  }

  public boolean deleteAll() {
    return redisRepository.deleteAll();
  }

  public List<OrderRequestDto> findAll() {
    return redisRepository.findAll();
  }

  public List<BinaryMessage> getBinMessageList() {
    return redisRepository.findAll()
        .stream()
        .map(o -> {
          try {
            return mapper.writeValueAsBytes(o);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[0];
          }
        })
        .map(BinaryMessage::new)
        .collect(Collectors.toList());
  }

}
