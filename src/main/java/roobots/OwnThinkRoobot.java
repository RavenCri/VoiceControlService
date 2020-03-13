package roobots;

import com.alibaba.fastjson.JSONObject;
import util.HttpUtil;

public class OwnThinkRoobot {
    private   String url = "https://api.ownthink.com/bot";
    public JSONObject getWord(String word){
        String req = "appid=39ab1be8f6178a21b1fbfe3c4d5d64fe&userid=user&spoken="+word;
        try{
            JSONObject respJSON = JSONObject.parseObject(HttpUtil.sendPostRequest(url,req));
            if(respJSON != null && respJSON.getString("message").contains("success")){
                String replay = respJSON.getJSONObject("data").getJSONObject("info").getString("text");
                JSONObject obj = new JSONObject();
                obj.put("status","success");
                obj.put("data",replay);
                return obj;
            }
        }catch (Exception e){
            JSONObject obj = new JSONObject();
            obj.put("status","success");
            obj.put("data","服务器异常，请稍后再试。");
            return obj;
        }
        return null;
    }
}
