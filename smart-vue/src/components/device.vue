<template>
    <div>
        <div class="title"><i class="el-icon-position"></i>我的设备列表</div>

        <el-table :data="devices" style="width: 100%;">
            <el-table-column label="生产日期" width="200" style="height: 10px;">
                <template slot-scope="scope">
                    <i class="el-icon-time"></i>
                    <span style="margin-left: 10px">{{ scope.row.create_time }}</span>
                </template>
            </el-table-column>
            <el-table-column label="设备类型" width="300">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备类型: {{ scope.row.type }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.type }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="设备id" width="300">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备ID: {{ scope.row.deviceId }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.deviceId }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="primary" round style="display: block;width: 200px; margin: 30px auto;" @click='addDevice'>增加设备
        </el-button>
    </div>
</template>

<script>
    import qs from 'qs';
    export default {
        name: 'device',
        mounted() {
            this.initData()
            // console.log(this.devices)
        },
        data() {
            return {
                devices: []
            }
        },
        methods: {
            initData() {
                this.$axios.get('device/list').then(res => {
                    if (res.data.code == -1) {
                        this.$alert('您的token已失效，请重新登录！', '数据异常', {
                            confirmButtonText: '确定',
                            callback: action => {
                                localStorage.clear('token');
                                this.$router.push({ name: 'login' })
                            }
                        });

                    } else if (res.data.code == 200) {
                        this.devices = res.data.data;
                    }
                })
            },
            handleEdit(index, row) {
                console.log(index, row);
                this.$prompt('请输入要修改的产品ID', '修改设备id', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^\d{4}-\d{4}-\d{4}-\d{4}$/,
                    inputErrorMessage: '设备id格式不正确'
                }).then(({ value }) => {
                    if(value == this.devices[index]['deviceId']){
                        this.$message.error('请保证当前设备id与更新id不同！');
                        return
                    }
                    this.$confirm('确定要将设备id改为：' + value + '吗？', '更新设备编号', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$axios.post('device/update',
                            qs.stringify(
                                {
                                    "newDeviceId": value,
                                    "oldDeviceId": this.devices[index]['deviceId']
                                }
                            )).then(res => {
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
                }).catch(() => {

                });
            },
            handleDelete(index, row) {
                console.log(index, row);
                this.$confirm('确定要从您的账户下移除该设备吗？', '删除设备', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post('device/delete', qs.stringify({ "deviceId": this.devices[index]['deviceId'] })).then(res => {
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
            addDevice() {
                this.$prompt('请输入新的设备ID', '添加设备', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^\d{4}-\d{4}-\d{4}-\d{4}$/,
                    inputErrorMessage: '设备id格式不正确'
                }).then(({ value }) => {

                    this.$axios.post('device/add', qs.stringify({ "deviceId": value })).then(res => {
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