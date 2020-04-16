package roobots;

import com.alibaba.fastjson.JSONObject;
import com.web.result.Result;
import util.HttpUtil;

public class TuLingRoobot {
    private String url = "http://openapi.tuling123.com/openapi/api/v2";
    private String apiKey = "8d8f0859dea14f1eacf2a3d20a136a27";
    private String userId = "373886";
    public Result getWord(String word){
        JSONObject reqJson = new JSONObject();
        JSONObject perception = new JSONObject();
        // 输入的文本信息
        JSONObject inputText = new JSONObject();
        inputText.put("text",word);

        perception.put("inputText",inputText);

        JSONObject userInfo = new JSONObject();
        userInfo.put("apiKey",apiKey);
        userInfo.put("userId",userId);

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

                Result result = new Result();
                result.setCode(200);
                result.setMsg(replay);


                return result;

            }
        }
        Result result = new Result();
        result.setCode(400);
        result.setMsg("服务器异常，请稍后再试。");
        return result;


    }
}
