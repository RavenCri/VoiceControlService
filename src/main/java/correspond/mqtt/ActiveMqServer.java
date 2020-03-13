package correspond.mqtt;

/**
 * @Auther: raven
 * @Date: 2019/11/18 00:08
 * @Description:
 */
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Hello world!
 */
public class ActiveMqServer {

    public static void main(String[] args) throws Exception {
        thread(new HelloWorldProducer(), false);
        System.in.read();
       //thread(new HelloWorldConsumer(), false);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(simpleDateFormat.parse("2020-03-05"));
//
//        System.out.println(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
//        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH,1);
//
//        String format = simpleDateFormat.format(calendar.getTime());
//        System.out.println(format);


    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
        @Override
        public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();

                connection.setClientID("ravenProducer");

                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                //创建发布/订阅模式消息
                Topic createTopic = session.createTopic("test");//        非持久订阅
                //创建消费者
                MessageConsumer createConsumer = session.createConsumer(createTopic);
                //设置消费者监听
                createConsumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println("接收的消息为：" + textMessage.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JMSException e) {
                e.printStackTrace();
            }
               /* //创建队列目标
                Destination destination = session.createQueue("TEST.FOO");

                //创建一个生产者
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a messages
                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send the message
                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);

                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }*/

        }
    }
    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
        @Override
        public void run() {
            try {

                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.setClientID("ravenConsumer");
                connection.start();

                connection.setExceptionListener(this);

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait for a message
                Message message = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }

                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }
}