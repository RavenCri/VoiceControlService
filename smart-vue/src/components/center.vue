<template>
    <div>
        <el-container>
            <el-aside style="position: fixed;height: 100%;" width="250px">
                <img src="../assets/ico.png" style="width:185px;">
                <el-menu :default-openeds="['1', '2','3']">
                    <!-- <div v-for='(item,index) in allRouter'>
                        <el-submenu index="item">
                            <template slot="title"><i :class="item.meta.icon"></i>{{item.meta.title}}</template>
                            <div v-for='(item2,index2) in item '>
                                <el-menu-item index="item-item2">
                                    <router-link to="/center/device">{{item2.meta.title}}</router-link>
                                </el-menu-item>
                            </div>

                        </el-submenu>
                    </div> -->
                    <el-submenu index="1">
                        <template slot="title"><i class="el-icon-cpu"></i><span slot="title">设备中心</span></template>
                        <el-menu-item index="1-1">
                            <router-link to="/center/device"><span style="display: block;">我的设备</span></router-link>
                        </el-menu-item>
                    </el-submenu>
                    <el-submenu index="2">
                        <template slot="title"><i class="el-icon-menu"></i><span slot="title">安全中心</span></template>
                        <el-menu-item index="2-1">
                            <router-link to="/center/updateInfo"><span style="display: block;">修改密码</span></router-link>
                        </el-menu-item>
                    </el-submenu>

                    <el-submenu index="3" v-if='roles.indexOf("manager") !=-1'>
                        <template slot="title"><i class="el-icon-s-ticket"></i><span slot="title">管理专区</span></template>
                        <el-menu-item index="3-1" v-if='roles.indexOf("manager:device") !=-1'>
                            <router-link to="/center/manager/deviceManager"><span style="display: block;">设备管理</span>
                            </router-link>
                        </el-menu-item>
                        <el-menu-item index="3-2" v-if='roles.indexOf("manager:user") !=-1'>
                            <router-link to="/center/manager/userManager"><span style="display: block;">用户管理</span>
                            </router-link>
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>
            <el-container>
                <el-header>
                    <el-dropdown style="margin-left: 80%">
                        <i class="el-icon-setting" style="margin-right: 15px"></i>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item><span @click='logout' style="display: block;">退出</span></el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <span
                        v-if="userInfo !=null">您好，{{userInfo.nickname}}({{userInfo.accountLevel==0?'普通用户':'管理员'}})</span>
                </el-header>
                <el-main>
                    <router-view></router-view>
                    <div v-if="this.$route.path=='/center'">
                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <span>系统特点</span>
                            </div>
                            <div>
                                <p>权限管理、精准控制、智能聊天</p>
                            </div>
                        </el-card>
                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <span>系统接入</span>
                            </div>
                            <div>
                                <p>后台接口：http://api.raven520.top:5500</p>
                            </div>
                        </el-card>
                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <span>关于</span>
                            </div>
                            <div>
                                <p>本设计由raven设计，bug测试call:18856975969</p>
                            </div>
                        </el-card>
                    </div>
                </el-main>
                <el-footer style="text-align: center;">Design by @Raven 2020</el-footer>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import { userInfo } from '@/common/api/user.js'
    import store from '@/store/index'
    import { getToken, removeToken, getUserInfo, removeUserInfo } from '@/utils/app'
    import router from '../router/index'
    export default {
        name: 'center',

        mounted() {
            this.initUserInfo();
            this.allRouter = store.getters['permission/allRouter']
            this.roles = store.getters['permission/roles']

        },
        data() {
            return {
                userInfo: null,
                allRouter: '',
                roles: ''
            }
        },
        methods: {

            initUserInfo() {
                let info = getUserInfo();

                if (info == null) {
                    userInfo().then(res => {

                        if (res.data.code == 200) {
                            this.userInfo = res.data.data
                            localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
                        }
                    },err=>{
                       console.log(err)
                        
                    })
                } else {
                    this.userInfo = info
                }

            },
            logout() {
                removeToken()
                removeUserInfo()
                this.$router.push({ name: 'index' })
            }
        }

    }

</script>

<style>
    .el-header {
        background-color: #648cff;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-footer {}

    .el-main {
        background-color: white;
        margin-left: 250px;
        height: 630px;
    }

    .el-aside {
        background-color: #648cff;
        color: #333;
        text-align: center;
    }

    a {
        text-decoration: none;
        color: black;
    }
</style>