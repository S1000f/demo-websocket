package my.demo.tradingview.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
public final class SecurityUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

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
    String token = converted.substring(6);

    return validateToken(token) ? token : null;
  }

  /**
   * <p>SHA3-256 해시 함수</p>
   *
   * @param target 해싱할 대상
   * @return 해시 값
   * @since 0.1.0
   */
  public static byte[] getSha3bit256(byte[] target) {
    SHA3.Digest256 digest256 = new SHA3.Digest256();
    return digest256.digest(target);
  }

  /**
   * <p>SHA3-256 해시 함수</p>
   *
   * @param utf8 UTF-8 인코딩 문자열
   * @return 해시 값 hex string
   * @since 0.1.0
   */
  public static String getSha3bit256(String utf8) {
    byte[] target = utf8.getBytes(StandardCharsets.UTF_8);
    return Hex.toHexString(getSha3bit256(target));
  }

  public static <T> String buildRedisKey(T requestDto) throws JsonProcessingException {
    return getSha3bit256(mapper.writeValueAsString(requestDto));
  }

}
