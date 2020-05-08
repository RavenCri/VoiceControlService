<template>
    <div>
        <div class="title"><i class="el-icon-position"></i>用户管理</div>
        <el-table :data="users" style="width: 100%;" :border=true :stripe=true :highlight-current-row=true>
            <el-table-column label="用户名" width="120">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备类型: {{ scope.row.username }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.username }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="用户昵称" width="300">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备ID: {{ scope.row.nickname }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type='success'>{{ scope.row.nickname }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="设备组" width="300">
                <template slot-scope="scope">
                    <el-tag size="medium" type='danger' style="margin-bottom: 10px;" v-if="scope.row.devices==0">无
                    </el-tag>
                    <el-popover trigger="hover" placement="top">
                        <p>设备组</p>
                        <div slot="reference" class="name-wrapper" v-for="(item,index) in scope.row.devices">
                            <el-tag size="medium" type='warning' style="margin-bottom: 10px;">{{ item.deviceId }}
                            </el-tag>
                        </div>

                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="账号状态" width="300">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>账号状态: {{ scope.row.userAuth.usable?'正常':'封禁'   }}</p>
                        <div slot="reference" class="name-wrapper">
                            <div v-if='scope.row.userAuth.usable==true'>

                                <el-tag size="medium" type='success'>正常</el-tag>
                            </div>
                            <div v-else-if='scope.row.userAuth.usable==false'>
                                <el-tag size="medium" type='success'>封禁</el-tag>
                                <el-tag size="medium" type='success'>原因：{{scope.row.userAuth.reason}}</el-tag>
                            </div>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" @click="cancleForbidden(scope.$index, scope.row)" v-if="scope.row.userAuth.usable==false">取消封禁
                    </el-button>
                    <el-button size="mini" type="danger" @click="forbidden(scope.$index, scope.row)">封禁账户</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="primary" round style="display: block;width: 200px; margin: 30px auto;" @click='flushData'>
            刷新数据
        </el-button>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: 'userManager',
        data() {
            return {
                users: []
            }
        },
        mounted() {
            this.initData()
        },
        methods: {
            initData() {
                this.$axios.get('account/userList').then(res => {
                    if (res.data.code == 200) {

                        this.users = res.data.data;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                })
            },
            forbidden(index, row) {
                this.$prompt('封禁原因', '输入', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(({ value }) => {
                    this.$axios.post('account/close', qs.stringify({
                        "username": this.users[index]['username'],
                        "reason": value
                    })).then(res => {
                        if (res.data.code == 200) {
                            this.$message({
                                type: 'success',
                                message: res.data.msg
                            });
                            this.initData();
                        } else {
                            this.$message.error(res.data.msg);
                        }
                    })
                }).catch((err) => {
                    console.log(err)
                });
            },
            cancleForbidden(index, row) {
                this.$confirm('确定要取消封禁该用户吗', '取消封禁', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log(JSON.stringify(this.updateForm))
                    this.$axios.post('account/open',
                        qs.stringify({
                            "username": this.users[index]['username'],

                        })).then(res => {
                            if (res.data.code == 200) {
                                this.$message({
                                    type: 'success',
                                    message: res.data.msg
                                });
                                
                                this.initData();
                            } else {
                                this.$message.error(res.data.msg);
                            }
                        })
                }).catch((err) => {
                });
            },
            flushData() {
                this.$notify({
                    title: '刷新数据',
                    message: '刷新成功~',
                    type: 'success'
                });
                this.initData();
            }
        }
    }
</script>

<style scoped>
 .title {
        text-align: center;
        margin: 10px auto 20px;
        font-size: 28px;
    }
</style>