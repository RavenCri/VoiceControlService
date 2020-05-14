<template>
    <div style="overflow: hidden;height:690px">
        <img src="../assets/znjj.jpg" alt=""
            style="width:100%;height: 110%; background-repeat: repeat-y; position: absolute;z-index: -1;top:-50px">
        <div class="content">
            <div v-show="showlogin" id='login'>

                <h1>科睿智能家居测试系统</h1>
                <div style="height: 60px;"></div>
                <div class="box">
                    <span>账号：</span><input type="text" v-model='loginForm.username'>
                </div>
                <div style="height: 30px;"></div>
                <div class="box">
                    <span>密码：</span><input type="password" v-model='loginForm.password'>
                </div>
                <div style="height: 5px;"></div>
                <div class="bt">
                    <el-button type="primary" round style="margin-bottom: 20px;" @click='userLogin'>登录</el-button>
                    <p style="cursor: pointer;" @click='goRegister'>没有账号？前去注册</p>
                    <p style="cursor: pointer;margin-top: 20px;color: greenyellow;" @click="goIndex">Design by @科睿工作室
                    </p>
                </div>

            </div>
            <div v-show="showregister" id='register'>

                <h1>注册账号</h1>
                <div class="box">
                    <span>账号：</span><input type="text" v-model='registerForm.username'>
                </div>
                <div class="box">
                    <span>昵称：</span><input type="text" v-model='registerForm.nickname'>
                </div>
                <div class="box">
                    <span>密码：</span><input type="password" v-model='registerForm.password'>
                </div>
                <div class="box">
                    <span>确认密码：</span><input type="password" v-model='registerForm.password2'>
                </div>
                <div class="bt">
                    <el-button type="primary" round style="margin-bottom: 20px;" @click='userRegister'>注册</el-button>
                    <p style="cursor: pointer;" @click='goLogin'>已有账号？前去登录</p>
                    <p style="cursor: pointer;margin-top: 20px;color: greenyellow;" @click='goIndex'>Design by @科睿工作室
                    </p>
                </div>

            </div>
        </div>

    </div>
</template>

<script>
    import qs from 'qs';
    import {login,register} from '@/common/api/user.js'
    export default {
        name: 'login',
        mounted() {
            if(typeof(localStorage.token) != 'undefined'){
                this.$router.push({ name: 'center' });
            }
        },
        data() {
            return {
                showlogin: true,
                showregister: false,

                loginForm: {
                    username: '',
                    password: ''
                },
                registerForm: {
                    username: '',
                    nickname: '',
                    password: '',
                    password2: ''
                }
            }
        },
        methods: {
            goRegister() {
                this.showlogin = false
                this.showregister = true;
            },
            goIndex() {
                window.location.href = '/'
            },
            goLogin() {
                this.showlogin = true;
                this.showregister = false;
            },
            userLogin() {
                for (var o in this.loginForm) {
                    if (this.loginForm[o] == '') {
                        this.$message.error("请将参数填写完整再提交吧~");
                        return;
                    }
                }
                const loading = this.$loading({
                    lock: true,
                    target: "#login",
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.3)'
                });
                login(this.loginForm).then(res => {
                    loading.close();
                    if (res.data.code == 200) {
                        localStorage.setItem('token', res.headers['token'])
                        localStorage.setItem('userInfo', JSON.stringify(res.data.data))
                        this.$router.push({ name: 'center', });
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).catch(err=>console.log(err))
            },
            userRegister() {
                for (var o in this.loginForm) {
                    if (this.registerForm[o] == '') {
                        this.$message.error("请将参数填写完整再提交吧~");
                        return;
                    }
                }
                const loading = this.$loading({
                    lock: true,
                    target: "#register",
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.3)'
                });
                register(this.registerForm).then(res => {
                    loading.close();
                    if (res.data.code == 200) {
                        this.$message({
                            message: res.data.msg,
                            type: 'success'
                        });
                    } else {
                        this.$message.error(res.data.msg);
                    }
                })
                
            }
        }

    }

</script>

<style scoped>
    h1 {
        letter-spacing: 8px;
    }

    .content {
        width: 500px;
        height: 420px;
        margin: 180px auto;
        background-color: rgba(0, 0, 0, 0.5);
        padding-top: 50px;
        text-align: center;
        border: 1px solid white;
        color: white;
        user-select: none;

    }

    .box {
        width: 360px;
        margin: 15px auto;
    }

    .box span {
        font-size: 16px;
        display: inline-block;
        width: 80px;
        position: relative;
        top: 5px;
    }

    .box input {
        width: 200px;
        height: 40px;
        background-color: transparent;
        border-bottom: 1px solid whitesmoke;
        font-size: 16px;
        /* 不显示鼠标选中的框 */
        outline: none;
        color: honeydew;
        padding-left: 20px;
        display: inline-block;
        margin-left: 5px;
        letter-spacing: 3px;

    }

    .box input:focus {
        border: solid greenyellow;
        border-radius: 20px;
        line-height: 16px;
        transform: scale(1);
    }

    .bt {
        width: 260px;
        margin: 0 auto;
    }
</style>