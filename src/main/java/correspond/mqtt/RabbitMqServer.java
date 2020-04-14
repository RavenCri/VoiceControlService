package correspond.mqtt;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import util.RMConnectUtil;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-07 19:33
 **/
public class RabbitMqServer {
    public static String id = "0000-0000-0000-0000";

    public static void main(String[] args) throws Exception {
        Connection connection = RMConnectUtil.getConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 消息发送
        String message = "led_1_1";
        channel.basicPublish("amq.topic", id, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("Topic-Send：" + message);



    }
}
