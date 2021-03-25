package my.demo.tradingview.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto implements CacheableMessage {

  private String marketPair;
  private Boolean isBuy;
  private BigDecimal price;
  private BigDecimal amount;
  private String orderType;
  private String userIdx;
  private Long date;

  public void setClose() {
    this.orderType = "CLOSE";
  }
}
