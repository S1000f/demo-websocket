package my.demo.tradingview.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequestDto {

  private String marketPair;
  private Boolean isBuy;
  private BigDecimal price;
  private BigDecimal amount;
  private String orderType;
}
