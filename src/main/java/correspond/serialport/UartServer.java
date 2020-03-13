package correspond.serialport;

import cn.hutool.core.util.RuntimeUtil;
import org.springframework.stereotype.Service;

@Service
public class UartServer {
   private static String port = "COM3";
   private static String bps = "9200";
   public  boolean sendMsg(String  msg){

      String path = getClass().getResource("/script/test.py").getPath().substring(1);
      String res = RuntimeUtil.execForStr(String.format("py %s -m=%s -p=%s -b=%s", path, msg, port, bps));
      System.out.println(res);
      return true;
   }
}
