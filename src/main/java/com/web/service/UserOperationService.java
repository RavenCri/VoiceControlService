package com.web.service;

import com.alibaba.fastjson.JSONObject;
import com.web.pojo.Device;
import init.JSONFileInit;
import correspond.serialport.UartServer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roobots.OwnThinkRoobot;
import roobots.TuLingRoobot;

import java.util.List;
import java.util.Random;

@Service
public class UserOperationService {
    @Autowired
    public AmqpTemplate mqttServer;
    TuLingRoobot tuLingRoobot = new TuLingRoobot();
    OwnThinkRoobot ownThinkRoobot = new OwnThinkRoobot();
    @Autowired
    DeviceService deviceService;
    @Autowired
    UartServer uartServer;

    /**
    * @Description: 用来获取并返回智能回复的话语，并发送指令至MQTT服务器
    * @Param: [userId, word] 第一个参数为用户id
     *                       第二个参数为用户说的话，用来匹配回复内容
    * @return: java.lang.String
    * @Author: raven
    * @Date: 2020/4/10
    */
    public String getWord(String userId,String word){
        JSONObject wordMap = JSONFileInit.wordMap;
        System.out.println(word);
        List<Device> userDevices = deviceService.findDevice(userId);
        String key;
        if(word.contains("灯") && word.contains("开") ){ //默认开1号房间的灯
            key = "开灯";
        }else if(word.contains("灯") && word.contains("关")){
            key = "关灯";
            //操作 客厅 的灯
        } else if(word.contains("灯") && word.contains("开") && word.contains("客厅")  ){
            key = "开灯";
        } else if(word.contains("灯") && word.contains("关")  && word.contains("客厅")  ){
            key = "关灯";
            //操作所有灯
        }else if(word.contains("灯") && word.contains("关") && (word.contains("都")|| word.contains("全")|| word.contains("所有"))){
            key = "关灯";
        }else if(word.contains("灯") && word.contains("开") && (word.contains("都")|| word.contains("全")|| word.contains("所有"))){
            key = "开灯ALL";
        } else if(word.contains("蹦") || word.contains("迪")){
            key = "蹦迪";
        }else if(word.contains("风扇") && (word.contains("快")||word.contains("加")) ){
            key = "风扇快";
        }else if(word.contains("风扇") && (word.contains("慢")||word.contains("减")) ){
            key = "风扇慢";
        }else if(word.contains("风扇") && word.contains("转") ){
            key = "风扇正转";
        }else if(word.contains("风扇") && word.contains("反") && word.contains("转") ){
            key = "风扇反转";
        }else if(word.contains("风扇") && word.contains("停") ){
            key = "风扇停止";
        } else{
            // 调用只能回复库
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
        // 如果智能回复库没有成功，获取本地回复列表
        JSONObject currIndexJSON = wordMap.getJSONObject(key);
        if(currIndexJSON.containsKey("code")){
            mqttServer.convertAndSend("amq.topic", userDevices.get(0).getDeviceId(), currIndexJSON.getString("code"));
        }
        List<String> replayList= JSONObject.parseArray(currIndexJSON.getString("word"),String.class);
        // 随机引索
        int len = new Random().nextInt(replayList.size());
        System.out.println("回复："+replayList.get(len));
        JSONObject obj = new JSONObject();
        obj.put("status","success");
        obj.put("data",replayList.get(len));
        return obj.toJSONString();
    }
}
