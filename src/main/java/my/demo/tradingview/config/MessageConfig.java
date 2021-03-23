package my.demo.tradingview.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import my.demo.tradingview.model.OrderRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

@Slf4j
@Configuration
public class MessageConfig {

  @Bean
  public MappingJackson2MessageConverter messageConverter() {
    Map<String, Class<?>> typeIdMapping = new HashMap<>();
    typeIdMapping.put("order", OrderRequestDto.class);

    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
    messageConverter.setTypeIdPropertyName("typeId");
    messageConverter.setTypeIdMappings(typeIdMapping);

    return messageConverter;
  }

}
