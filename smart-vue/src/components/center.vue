<template>
    <div>

        <el-container>
            <el-aside style="position: fixed;height: 100%;" width="250px">
                <el-menu :default-openeds="['1', '2']">
                    <el-submenu index="1">
                        <template slot="title"><i class="el-icon-message"></i>设备管理</template>
                        <el-menu-item index="1-1">
                            <router-link to="/center/device">查看设备</router-link>
                        </el-menu-item>
                    </el-submenu>
                    <el-submenu index="2">
                        <template slot="title"><i class="el-icon-menu"></i>安全中心</template>
                        <el-menu-item index="2-1">
                            <router-link to="/center/updateInfo">修改密码</router-link>
                        </el-menu-item>
                    </el-submenu>

                </el-menu>
            </el-aside>
            <el-container>
                <el-header>
                    <el-dropdown style="margin-left: 90%">
                        <i class="el-icon-setting" style="margin-right: 15px"></i>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item><span @click='logout'>退出</span></el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <span>您好，{{nickname}}</span>
                </el-header>
                <el-main>
                    <router-view></router-view>
                </el-main>
                <el-footer style="text-align: center;">Design by @Raven 2020</el-footer>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: 'center',
        mounted() {
            this.initUserInfo();
        },
        data() {
            return {
                nickname: ''
            }
        },
        methods: {
            initUserInfo() {
                this.$axios.get('account/userInfo').then(res => {

                    if (res.data.code == -1) {
                        this.$alert('您的token已失效，请重新登录！', '数据异常', {
                            confirmButtonText: '确定',
                            callback: action => {
                                localStorage.clear('token');
                                this.$router.push({ name: 'login' })
                            }
                        });

                    } else if (res.data.code == 200) {

                        this.nickname = res.data.data['nickname']
                    }

                })
            },
            logout() {
                localStorage.clear('token');
                this.$router.push({ name: 'index' })
            }
        }

    }

</script>

<style>
    .el-header,
    .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-main {
        background-color: white;
        margin-left: 250px;
        height: 630px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;

    }

    a {
        text-decoration: none;
        color: black;
    }
</style>