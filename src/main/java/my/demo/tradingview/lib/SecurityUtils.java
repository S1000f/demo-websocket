package my.demo.tradingview.lib;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
public final class SecurityUtils {

  private SecurityUtils() {
  }

  // FIXME:
  public static boolean validateToken(String token) {
    return StringUtils.hasText(token);
  }

  // FIXME:
  public static boolean hasToken(BinaryMessage message) {
    byte[] sign = new byte[5];
    new BinaryMessage(message.getPayload().array())
        .getPayload()
        .get(sign, 0, 5);

    return new String(sign, StandardCharsets.UTF_8).equals("token");
  }

  // FIXME:
  public static String extractToken(BinaryMessage message) {
    String converted = new String(message.getPayload().array(), StandardCharsets.UTF_8);
    String token = converted.substring(5);

    return validateToken(token) ? token : null;
  }

}
