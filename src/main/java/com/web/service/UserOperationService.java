package com.web.service;

import com.alibaba.fastjson.JSONObject;
import com.init.JSONFileInit;
import com.roobots.OwnThinkRoobot;
import com.roobots.TuLingRoobot;
import com.web.controller.UserDeviceController;
import com.web.dao.DeviceRepository;
import com.web.dao.UserDeviceRepository;
import com.web.pojo.Device;
import com.web.pojo.UserDevice;
import com.web.result.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserOperationService {
    @Autowired
    public AmqpTemplate mqttServer;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    TuLingRoobot tuLingRoobot = new TuLingRoobot();
    OwnThinkRoobot ownThinkRoobot = new OwnThinkRoobot();
    @Autowired
    UserDeviceRepository userDeviceRepository;
    @Autowired
    DeviceRepository deviceRepository;


    /**
    * @Description: 用来获取并返回智能回复的话语，并发送指令至MQTT服务器
    * @Param: [userId, word] 第一个参数为用户id
     *                       第二个参数为用户说的话，用来匹配回复内容
    * @return: java.lang.String
    * @Author: raven
    * @Date: 2020/4/10
    */
    public Result getWord(String userId,String word,String deviceId,String platForm){
        JSONObject wordMap = JSONFileInit.wordMap;
        System.out.println(word);

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
        } else if(word.contains("室内") && word.contains("状态")  ){
            key = "家居状态";
        } else{
            // 调用只能回复库
            Result tuLingWord = tuLingRoobot.getWord(word);
            Integer statusCode1 = tuLingWord.getCode();
            if(statusCode1 == 200){
                return tuLingWord;
            }
            Result ownThinkRoobotWord = ownThinkRoobot.getWord(word);
            int statusCode2 = ownThinkRoobotWord.getCode();
            if(statusCode2 == 200){
               return ownThinkRoobotWord;
            }
            key = "未知";
        }
        // 如果智能回复库没有成功，获取本地回复列表
        JSONObject currIndexJSON = wordMap.getJSONObject(key);
        UserDevice userDevice = userDeviceRepository.findByUserIdAndDeviceId(userId, deviceId);
        if(userDevice == null){
            Result result = new Result();
            result.setCode(404);
            result.setMsg("该设备不存在或不在您的账户管理范围之内，有问题请联系管理员哦~");
            return result;
        }
        if(currIndexJSON.containsKey("code")){

            Device device = deviceRepository.findByDeviceId(userDevice.getDeviceId());
            // 如果设备未找到
            if(device == null){
                Result result = new Result();
                result.setCode(404);
                result.setMsg("设备未找到。");
                return result;
            }
            if(!UserDeviceController.OnlineDevice.containsKey(device.getDeviceId())){
                Result result = new Result();
                result.setCode(404);
                result.setMsg("您的设备当前不在线哦，曼拉无法帮您，请检查设备是否可以正常通信~");
                return result;
            }
            String subscribe =  device.getDeviceId()+"-"+device.getDeviceKey();
            JSONObject data = new JSONObject();
            data.put("code",currIndexJSON.getString("code"));
            data.put("platForm",platForm);
            String sendData = JSONObject.toJSONString(data);
            System.out.println(sendData);
           // mqttServer.convertAndSend("amq.topic",subscribe , sendData);
            rabbitTemplate.convertAndSend("amq.topic",subscribe , sendData);
        }
        List<String> replayList= JSONObject.parseArray(currIndexJSON.getString("word"),String.class);
        Result result = new Result();
        System.out.println(replayList);
        if(replayList.size() > 0){
            // 随机引索
            int len = new Random().nextInt(replayList.size());
            result.setCode(200);
            System.out.println("回复："+replayList.get(len));
            result.setMsg(replayList.get(len));
        }
        return result;
    }


}
