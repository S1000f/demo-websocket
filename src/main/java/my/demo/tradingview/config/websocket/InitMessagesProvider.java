package my.demo.tradingview.config.websocket;

import java.util.List;
import org.springframework.web.socket.BinaryMessage;

public interface InitMessagesProvider {

  List<BinaryMessage> getInitialMessageList();

}
