package server;

import com.alibaba.fastjson.JSONObject;
import config.JSONFileInit;
import correspond.serialport.UartServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roobots.OwnThinkRoobot;
import roobots.TuLingRoobot;

import java.util.List;
import java.util.Random;

@Service
public class UserOperationServer {

    TuLingRoobot tuLingRoobot = new TuLingRoobot();
    OwnThinkRoobot ownThinkRoobot = new OwnThinkRoobot();
    @Autowired
    UartServer uartServer;
    public String getWord(String word){
        JSONObject wordMap = JSONFileInit.wordMap;


        String key;
        if(word.contains("灯") && word.contains("开") ){
            key = "开灯";
            uartServer.sendMsg("LED_1");
        }else if(word.contains("灯") && word.contains("关")){
            key = "关灯";
            uartServer.sendMsg("LED_0");
        } else if(word.contains("蹦") || word.contains("迪")){
            key = "蹦迪";
          //  sendInstruct("CMD_LED_2_2");
        }else if(word.contains("风扇") && (word.contains("快")||word.contains("加")) ){
            key = "风扇快";
        }else if(word.contains("风扇") && (word.contains("慢")||word.contains("减")) ){
            key = "风扇慢";
        }else{


            JSONObject tuLingWord = tuLingRoobot.getWord(word);
            String statusOfTuLing = tuLingWord.getString("status");
            if(statusOfTuLing.equals("success")){
                return tuLingWord.toJSONString();
            }

            JSONObject ownThinkRoobotWord = ownThinkRoobot.getWord(word);
            String statusOfOwnThink = ownThinkRoobotWord.getString("status");
            if(statusOfOwnThink.equals("success")){
               return ownThinkRoobotWord.toJSONString();
            }
            key = "未知";
        }

        // 获取所有的回复列表
        List<String> replayList= JSONObject.parseArray(wordMap.getString(key ),String.class);
        // 随机引索
        int len = new Random().nextInt(replayList.size());
        System.out.println("回复："+replayList.get(len));
        JSONObject obj = new JSONObject();
        obj.put("status","success");
        obj.put("data",replayList.get(len));
        return obj.toJSONString();
    }
}
