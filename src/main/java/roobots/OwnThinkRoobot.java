package roobots;

import com.alibaba.fastjson.JSONObject;
import com.web.result.Result;
import util.HttpUtil;

public class OwnThinkRoobot {
    private   String url = "https://api.ownthink.com/bot";
    public Result getWord(String word){
        String req = "appid=39ab1be8f6178a21b1fbfe3c4d5d64fe&userid=user&spoken="+word;
        try{
            JSONObject respJSON = JSONObject.parseObject(HttpUtil.sendPostRequest(url,req));
            if(respJSON != null && respJSON.getString("message").contains("success")){
                String replay = respJSON.getJSONObject("data").getJSONObject("info").getString("text");
                Result result = new Result();
                result.setCode(200);
                result.setMsg("success");
                result.setData(replay);

                return result;
            }
        }catch (Exception e){

            Result result = new Result();
            result.setCode(400);
            result.setMsg("服务器异常，请稍后再试。");
            return result;
        }
        return null;
    }
}
