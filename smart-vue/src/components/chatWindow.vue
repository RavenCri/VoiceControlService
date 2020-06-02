<template>
    <div>
        <el-dialog title="曼拉小姐姐：" :visible.sync="roobotWindow" width="50%" :before-close="handleCloseRoobotWindow"
            :close-on-click-modal=false :close-on-press-escape=false>
            <div style="display: flex;flex-direction: column;">
                <div id='chatUI' class="inner-container chatUI">
                    <div v-for="(item,index) in chatMsg">
                        <div v-if="item.orient==='left'" class="left">
                            <div style="display: flex;margin: 15px;">
                                <div style="width: 80px;">
                                    <img src="../assets/ml.jpg" width="60px" alt="" class="head_img">
                                </div>
                                <div class="msg">
                                    <span>{{item.nickname}}：{{item.time}}</span>
                                    <p>{{item.msg}}</p>
                                </div>
                            </div>
                        </div>
                        <div v-else-if="item.orient==='right'" class="right">
                            <div style="display: flex;margin: 15px;position: relative;left: 180px;">

                                <div class="msg">
                                    <span>{{item.nickname}}：{{item.time}}</span>
                                    <p>
                                        {{item.msg}}</p>
                                </div>
                                <div style="width: 100px;">
                                    <img src="../assets/me.jpg" width="60px" alt="" class="head_img">
                                </div>
                            </div>


                        </div>

                    </div>
                </div>

            </div>
            <div>
                <textarea cols="78" rows="3"
                    style="height: 100px;border: 2px solid black;font-size:17px;border-radius: 20px;outline:none;padding:10px;"
                    v-model="currentMsg" v-on:keyup.enter="sendMsg" placeholder="输入一段信息..."></textarea>
            </div>

            <el-button type="primary" @click="sendMsg" style="position: relative;left: 600px;top:10px">发 送
            </el-button>

        </el-dialog>
    </div>
</template>

<script>
    import { formatDate } from '../common/dateUtil.js'
    // 引入
    import SockJS from 'sockjs-client';
    import Stomp from 'stompjs';
    import { robotreplay } from '@/common/api/user.js'
    import { mqttServerAddress } from '@/common/config.js'
    export default {
        name: 'chatWindow',
        data() {
            return {
                roobotWindow: false,
                chatMsg: [
                    { "orient": "left", "msg": "你好啊~我是曼拉小姐姐，有什么吩咐的呢？", "time": "2020.04.16", "nickname": "曼拉" },
                ],
                normalFormat: 'yyyy-MM-dd hh:mm',
                currentMsg: '',
                currDeviceId: '',
                userInfo:'',
                ws:null
            }
        },
        created() {
          
            this.userInfo = JSON.parse(JSON.parse(localStorage.userInfo));
          
            this.mqttStart();
        },
        mounted() {
            this.chatMsg[0].time = formatDate(new Date(), this.normalFormat);
            
        },
        watch: {

            chatMsg: {
                handler(newValue, oldValue) {
                    for (let i = 0; i < newValue.length; i++) {
                        if (oldValue[i] != newValue[i]) {
                            console.log(newValue)
                        }
                    }
                    this.scrollBotton();
                }

            },
        },
        methods: {
          
            scrollBotton() {

                this.$nextTick(() => {
                    var div = document.getElementById('chatUI')
                    if(div != null)
                    div.scrollTop = div.scrollHeight
                })
            },
            sendMsg() {
                this.currentMsg = this.currentMsg.replace("\n", '')
                if (this.currentMsg !== '') {
                    this.chatMsg.push({ "orient": "right", "msg": this.currentMsg, "time": formatDate(new Date(), this.normalFormat), "nickname": localStorage.nickname })
                    robotreplay({
                        word:this.currentMsg,
                        deviceId:this.currDeviceId,
                        platForm:"web"
                    }).then(res => {
                        if (res.data.code == 200) {
                            this.$message({
                                type: 'success',
                                message: res.data.msg
                            });

                        } else {
                            this.$message.error(res.data.msg);
                        }
                        //如果消息不为空的话，消息为空说明是设备主动推来消息
                        if(typeof(res.data.msg) !="undefined" ){
                            this.chatMsg.push({ "orient": "left", "msg": res.data.msg, "time": formatDate(new Date(), this.normalFormat), "nickname": "曼拉" })
                        }
                       //this.ws.send("/message/toFriend", {}, JSON.stringify({'message': this.currentMsg,'toUser':'test'}));
                        this.currentMsg = ''
                    }).catch(err=>{})
                } else {
                    this.$message.warning('不可以发送空内容哦~');
                }

            },
            handleCloseRoobotWindow() {
                this.roobotWindow = false;
            },
            mqttStart() {
                console.log("进入mqtt初始化");
                
                this.ws = Stomp.over(new SockJS(`${mqttServerAddress}/ws?Authorization=`+localStorage.Authorization));
                this. ws.heartbeat.outgoing = 0;
                this.ws.heartbeat.incoming = 0;  
                this.ws.connect({
                    name: `${this.userInfo['username']}@web`,
                }, (frame) => {
                   
                    this.ws.subscribe('/user/topic/reply', (msg) => {
                        console.log( msg.body);
                        this.chatMsg.push({ "orient": "left", "msg": msg.body   , "time": formatDate(new Date(), this.normalFormat), "nickname": "曼拉" })
                    });
                    //系统通知
                    this.ws.subscribe('/topic/notice', (msg) => {
                        console.log("notice");
                        console.log(msg.body);
                        this.chatMsg.push({ "orient": "left", "msg": msg.body   , "time": formatDate(new Date(), this.normalFormat), "nickname": "曼拉" })
                    });
                    
                    //推送消息
                    //this.ws.send(`/message/web.${this.userInfo['username']}`, {}, JSON.stringify({ 'name': 'anumbrella' }));
                    
                });

            }
        }

    }
</script>

<style scoped>
    /* for Chrome 隐藏滚动条*/
    .inner-container::-webkit-scrollbar {
        display: none;
    }

    .chatUI {
        height: 350px;
        border: 2px solid black;
        margin-bottom: 20px;
        overflow-y: auto;
        overflow-x: hidden;
        border-radius: 20px;
        background-image: url('../assets/chat_bg.jpg');
    }

    .left {
        position: relative;
        left: 10px;
    }

    .left .msg {
        font-size: 16px;
        font-weight: bold;
        width: 50%;
    }

    .left .head_img {
        border-radius: 100%;
        position: relative;
        top: 10px
    }

    .left .msg span {
        display: block;
        margin-bottom: 10px;
        color: white;
    }

    .left .msg p {
        width: 180px;
        background-color: blanchedalmond;
        border-radius: 20px;
        padding: 10px
    }

    .right {
        position: relative;
        left: 80px;
    }

    .right .msg {
        font-size: 16px;
        font-weight: bold;
        width: 50%;
    }

    .right .head_img {
        border-radius: 100%;
        position: relative;
        top: 20px;
        left: 10px;
    }

    .right .msg span {
        text-align: right;
        display: block;
        margin-bottom: 10px;
        position: relative;
        left: -10px;
        color: white;
    }

    .right .msg p {
        width: 180px;
        background-color: green;
        color: honeydew;
        border-radius: 20px;
        padding: 10px;
        position: relative;
        left: 140px
    }
</style>