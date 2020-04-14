package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-14 18:43
 **/
public class MqttUtil {
    public static String  request(String url, String submitType, JSONObject data){

        HttpUriRequest httpClient = null;
        try {
            HttpHost host = new HttpHost("localhost", 15672);
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope("localhost",15672),new UsernamePasswordCredentials("guest","guest"));
            if(submitType.equalsIgnoreCase("put")){
                if(data == null){
                    throw new RuntimeException("data 不能为空");
                }
                httpClient = new HttpPut(url);
                httpClient.setHeader("Content-Type", "application/json;charset=UTF-8");
                httpClient.setHeader("Accept", "application/json");
                StringEntity entity = new StringEntity(data.toJSONString(), ContentType.APPLICATION_JSON);
                ((HttpPut)httpClient).setEntity(entity);
            }else if(submitType.equalsIgnoreCase("get")){
                httpClient = new HttpGet(url);

            }

            CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();

            HttpResponse response = client.execute(host, httpClient);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JSONArray getUserList(){
        String data = request("/api/users", "get", null);
        JSONArray objects = JSON.parseArray(data);
        return objects;
    }
    public static boolean checkUsername(String username){
        JSONArray userList = getUserList();
        for (int i = 0; i < userList.size(); i++) {
            JSONObject user = userList.getJSONObject(0);
            if( user.getString("name").equals(username)){
                return false;
            }
        }
        return true;
    }
    public static boolean CreateUser(String username,String password){
        JSONObject json = new JSONObject();
        json.put("password",password);
        json.put("tags","custom");
        request("/api/users/"+username, "put", json);

        return true;
    }
    public static void setPermission(String username){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("configure",".*");
        jsonObject.put("write",".*");
        jsonObject.put("read",".*");
        request("/api/permissions/%2f/"+username,"put",jsonObject);
    }
}
