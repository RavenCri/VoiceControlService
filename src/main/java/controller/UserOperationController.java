package controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import config.JSONFileInit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.web.bind.annotation.*;
import util.FileUtil;
import util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @Author raven
 * @Description
 * @Date  19:17
 * @Param
 * @return
 **/
@RestController
public class UserOperationController {
    @PostMapping("/login")

    public String userLogin(@RequestParam String username, @RequestParam String password) {
        if("123".equalsIgnoreCase(username) && "123".equalsIgnoreCase(password)){
            return "success";
        }else{
            return "false";
        }

    }
    @ResponseBody
    @GetMapping("/replay")
    public String getReturnWord(@RequestParam("word") String word) throws InterruptedException {
        System.out.println(word);
        if(word == null ||word.trim().equals("") ){
            return "您什么也没有说";
        }
        JSONObject wordMap = JSONFileInit.wordMap;
        String url = "http://openapi.tuling123.com/openapi/api/v2";
        String url2 = "https://api.ownthink.com/bot";
        java.lang.String key = null;
        if(word.contains("灯") && word.contains("开") ){
            key = "开灯";
            sendInstruct("CMD_LED_2_1");
        }else if(word.contains("灯") && word.contains("关")){
            key = "关灯";
            sendInstruct("CMD_LED_2_0");
        } else if(word.contains("蹦") || word.contains("迪")){
            key = "蹦迪";
            sendInstruct("CMD_LED_2_2");
        }else if(word.contains("风扇") && (word.contains("快")||word.contains("加")) ){
            key = "风扇快";
        }else if(word.contains("风扇") && (word.contains("慢")||word.contains("减")) ){
            key = "风扇慢";
        }else{
            JSONObject reqJson = new JSONObject();
            JSONObject perception = new JSONObject();
            // 输入的文本信息
            JSONObject inputText = new JSONObject();
            inputText.put("text",word);

            perception.put("inputText",inputText);

            JSONObject userInfo = new JSONObject();
            userInfo.put("apiKey","8d8f0859dea14f1eacf2a3d20a136a27");
            userInfo.put("userId","373886");

            reqJson.put("perception",perception);
            reqJson.put("userInfo",userInfo);
            String res = HttpUtil.sendPostRequest(url,reqJson.toString());
            if(res !=null){
                JSONObject resJSON = JSONObject.parseObject(res);
                System.out.println(resJSON.toJSONString());
                int resultCode = resJSON.getJSONObject("intent").getInteger("code");
                if(resultCode != 4003  ) {
                    //回复列表 取第一个
                    JSONObject json = JSONObject.parseObject(resJSON.getJSONArray("results").getString(0));
                    String replay = json.getJSONObject("values").getString("text");
                    System.out.println("回复："+replay);
                    JSONObject obj = new JSONObject();
                    obj.put("status","success");
                    obj.put("data",replay);
                    return obj.toJSONString();

                }else {
                    String req = "appid=39ab1be8f6178a21b1fbfe3c4d5d64fe&userid=user&spoken="+word;
                    try{
                        JSONObject respJSON = JSONObject.parseObject(HttpUtil.sendPostRequest(url2,req));
                        if(respJSON != null && respJSON.getString("message").contains("success")){
                            String replay = respJSON.getJSONObject("data").getJSONObject("info").getString("text");
                            JSONObject obj = new JSONObject();
                            obj.put("status","success");
                            obj.put("data",replay);
                            return obj.toJSONString();

                        }
                    }catch (Exception e){

                    }


                }

                System.out.println(resJSON);
            }



            key = "未知";
        }
        List<String> replayList= JSONObject.parseArray(wordMap.getString(key ),String.class);
        int len = new Random().nextInt(replayList.size());
        System.out.println("回复："+replayList.get(len));
        JSONObject obj = new JSONObject();
        obj.put("status","success");
        obj.put("data",replayList.get(len));
        return obj.toJSONString();

    }

    private void sendInstruct(String instruct) {
        String classPath = getClass().getClassLoader().getResource("").getPath().substring(1);
        try {
            Runtime runtime = Runtime.getRuntime();
            Process exec = runtime.exec(String.format("python %s/client.py --instruct=%s", classPath, instruct));
            exec.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}