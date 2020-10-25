毕业设计的项目，可以通过前端项目（任选一个）来远程控制家居设备（硬件模拟家居环境）

APP控制方式：语音唤醒、语音控制

WEB控制方式：聊天UI界面关键词触发
在线演示视频： 基于语音识别的远程控制系统设计

本系统一共分为三个板块，进行异步开发。

1.后端：

技术栈：

springboot：web框架

shiro：权限验证

spring data：持久化数据

JWT：替代token的解决方案

通信：RabbitMQ（使用了其提供的MQTT端口）

swagger: 接口文档
准备工作：

安装Mysql 8.0、redis、rabbitmq

同时rabbitmq需要安装插件以开启对相关协议的支持：

rabbitmq-plugins enable rabbitmq_stomp
rabbitmq-plugins enable rabbitmq_mqtt  
rabbitmq-plugins enable rabbitmq_web_stomp_examples  
知识准备：

了解JWT的应用原理
了解 Shiro执行的流程原理
了解STOMP协议
了解 springData的接口化开发原理
学会如何解决JWT过期的解决方案
整理的文档：如何利用springboot快速搭建一个消息推送系统
配置信息：

application.yml中配置了RabbitMQ的相关信息，以及JWT的部分配置。
application.properties中配置了mysql、redis```的相关信息
运行：

启动Redis
启动RabbitMQ
启动frp（可选）
修改相关配置信息，启动Springboot应用
后台接口：http://localhost:8080/swagger-ui.html

2.硬件端：

由于我是个手残党，对于硬件的兴趣远远低于对于软件的热爱，所以硬件的开发我选择基于Arduino平台（超级简单，因为都是C的开发风格）。

控制板：Arduino mega 2560 无线通信模块：ESP32 基础模块：LED灯、5V电机 Arduino工程代码：arduino工程代码 Arduino毕设总结教程：如何仅花20元成本用arduino平台建立自己的物联网应用

3.控制端（前端）：

采用了两种控制方案：

3.1 APP方案

项目地址：语音控制APP 使用本APP，用户登录自己的账户，附带语音唤醒功能： 只需轻轻一句：曼拉曼拉，帮我打开厨房的小灯。即可看到效果。

3.2 web方案

本项目的子文件工程 smart-vue 基于VUE、VUE-ElementUI、vuex、axios开发。 与后端消息通信协议：stomp

知识准备：

了解VUEX的原理
学习npm包管理工具
学习axios如何传送参数
学习如何生成动态路由
项目特点： 由权限动态生成路由、以及菜单。 权限默认分级：管理员、普通用户。 管理员：CRUD用户、CRUD出厂设备。 用户：CRUD自己购买的设备、以及控制设备。

配置信息

common/config.js配置MQTT服务，

config.index.js 配置axios访问后台接口

启动：

npm install 

npm start
前端首页展示：



普通用户登录：


管理员登录
adminuser

上图中，可以清楚地看到路由菜单是动态生成的。

当普通用户试图访问管理员页面时：

noAuthorized

4.项目框架

4.1 APP通信图

appset

4.2 多设备精准控制方案：mqttnet

4.3 整体方案allset

5.frp（内网穿透）:

因为我的服务器只有2GB内存，可怜.jpg。所以前端web方案以及使用的RabbitMQ消息队列、后台系统，我并没有部署至云端（也是为了方便调试），而是部署在本地。通过frp内网穿透。可以达到公网访问的效果。 你可以通过查看本项目的子文件夹frp/frpc.ini配置文件修改相关信息
5.1参考配置

frp服务端配置：

# 监听端口
bind_port = 7000
# HTTP端口
vhost_http_port = 5500
# token值
token=raven
启动：

cd ./frp
start /b frps -c ./frps.ini
frp客户端配置：

#服务器信息
[common]
server_addr = 139.196.46.x
# 服务器端口
server_port = 7000
token = raven
#配置前端内网穿透
[web-smart-vue] 
type = http         
local_port = 80
local_ip = 127.0.0.1
custom_domains = home.raven520.top
#配置后端内网穿透
[web-smart-springboot] 
type = http         
local_port = 8080
local_ip = 127.0.0.1
custom_domains = api.raven520.top
#配置mqtt端口内网穿透
[mqtt] 
type = tcp         
local_port = 1883
remote_port = 1883
启动：

cd ./frp
start /b frpc -c ./frpc.ini
TODO：

shiro 整合redis 完成缓存
token 过期，应该有续期功能。后期完善 RefreshToken
完善权限处理
完善后台管理系统
六、虚拟调试

没有硬件模块，想要调试效果，你需要安装一个==MQTT==客户端来模拟后台与硬件收发消息，这里面我推荐使用==MQTTFX==，是非常棒的一款MQTT客户端。下载安装即可。

配置详情：

SptingBoot:
因为只有硬件设备上电后，并会向服务器发送一个上线的请求，通知服务器该设备已上线，而虚拟调试只是调试是否可以再==MQTT客户端==中接收到消息，故并没有注册设备在线，所以需要注释掉判断在线的代码。

在src/main/java/com/web/controller/UserOperationController中的==getWord==方法注释如下代码：

 if(!UserDeviceController.OnlineDevice.containsKey(device.getDeviceId())){
                Result result = new Result();
                result.setCode(200);
                result.setMsg("您的设备当前不在线哦，曼拉无法帮您，请检查设备是否可以正常通信~");
                return result;
            }
同样在该方法中可以知道，设备监听的==topic==的值为：设备ID-设备密码

    String subscribe =  device.getDeviceId()+"-"+device.getDeviceKey();
 	rabbitTemplate.convertAndSend("amq.topic",subscribe , sendData);
知道上述规则后：

我们将==MQTTFX==启动，并启动前端服务，在前端登录账号。

普通用户：测试账号：==test== 测试密码：==test==

管理员：测试账号：==admin== 测试密码：==admin==

选择设备，并在数据库中查看设备密码（因为设备密码及其隐私，不能在前端展示。该设备密码只有在设备出厂的盒子的说明书可以找到！）。

selectdevices

找到设备密码之后，开始用==MQTT FX== 连接MQTT服务器，定订阅一个==subscribe==,如：

0000-0000-0000-0002-3rbc47r7rngjxq50

MQTTFXinit

然后我们通过前端点击控制按钮，并发送开灯：

openled

可以在MQTT客户端看到，它已经收到消息了！

showmsg

问题一：那么前端，怎么知道设备是否正常处理了消息呢？

用户前端已经监听了一个消息节点，所以可以通过发布消息到该节点即可。用户前端订阅该消息节点的代码位于：

smart-vue/src/components/chatWindow.vue的==mqttStart==方法。

因为后端和web端使用的都是stomp协议，所以使用MQTT客户端是无法直接推送消息到用户，因为stomp内部转化了我们的监听节点，会随机分配一个消息队列。如果你想这么做，那么，你可以打开

http://localhost:15672 点击Quenes，找到该用户所接收消息的队列名称。

queuerabbitmq

接下来，我们尝试用MQTT客户端向前端推送消息。

sendmsg

可以看到我们已经成功发送了消息，经过测试，使用MQTT-fx 发送中文数据会乱码，可能是由于MQTT-fx内部对于字符编码的设置，暂时没有尝试寻找解决办法，不过这也并不影响我们可以收发消息。

==还有一种更为简单的，也是本项目中 设备向用户发送消息所使用的方案：==

回看后端工程源码：

在src/main/java/com/correspond/mqtt/rabbitmq/controller/SendController.java的==sendMsgToUser==方法中可以看到提供的HTTP接口，当然你可以直接使用idea提供的HTTP调试工具直接post数据前端用户就可以收到指定的消息。