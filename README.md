> 毕业设计后端项目，前端可通过权限动态路由生成菜单，后端接口通过shiro进行权限验证

在线演示视频： [基于语音识别的远程控制系统设计](https://www.bilibili.com/video/BV1mg4y1B72M)
### 1.后端：

>技术栈：springboot、shiro(权限验证)、spring data(持久化数据)、 JWT(鉴权) 通信：RabbitMQ

### 2.硬件端：

由于我是个手残党，对于硬件的兴趣远远低于对于软件的热爱，所以硬件的开发我选择基于Arduino平台。

控制板：Arduino mega 2560
无线通信模块：ESP32
基础模块：LED、5V电机
Arduino工程代码：[arduino工程代码](https://github.com/RavenCri/arduino-GraduationProject)
Arduino毕设总结教程：[如何仅花20元成本用arduino平台建立自己的物联网应用](https://blog.csdn.net/huijiaaa1/article/details/105383074)

### 3.控制端（前端）：

采用了两种控制方案：

1. **APP方案**
    项目地址：[语音控制APP](https://github.com/RavenCri/VoiceControl)
    使用本APP，用户登录自己的账户，附带语音唤醒功能：
    只需轻轻一句：曼拉曼拉，帮我打开厨房的小灯。即可看到效果。
2. **web方案**
    本项目的子文件工程 **smart-vue**
    基于VUE、VUE-ElementUI、vuex、axios开发。
    与后端消息通信协议：stomp
    项目特点：
        由权限动态分配路由。
        权限默认分级：管理员、普通用户。
        管理员：管理用户、管理设备的出厂。
        用户：可以用来管理自己的设备、以及控制设备。

## frp（内网穿透）:
>因为我的服务器只有2GB内存，可怜.jpg。所以前端web方案以及使用的RabbitMQ消息队列、后台系统，我并没有部署至云端（也是为了方便调试），而是部署在本地。通过frp内网穿透。可以达到公网访问的效果。
 你可以通过查看本项目的子文件夹**frp/frpc.ini**配置文件修改相关信息

**TODO**

- ~~shiro 整合redis 完成缓存~~
- ~~token 过期，应该有续期功能。后期完善 RefreshToken~~
- 完善权限处理
- 完善后台管理系统