package com.init;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import com.util.FileUtil;

/**
 * @Auther: raven
 * @Date: 2019/11/12 22:58
 * @Description:
 */
@Component
public class JSONFileInit implements InitializingBean {

    public static JSONObject wordMap ;
    @Override
    public void afterPropertiesSet() throws Exception {
        String resStr  = FileUtil.readClassPathFile("word.json");
        wordMap = JSONObject.parseObject(resStr);
    }
}