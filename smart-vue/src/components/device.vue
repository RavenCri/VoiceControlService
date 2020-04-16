<template>
    <div>
        <div class="title"><i class="el-icon-position"></i>设备列表</div>
        <el-divider></el-divider>
        <el-table :data="devices" style="width: 100%;" :border=true :stripe=true :highlight-current-row=true>
            <el-table-column label="生产日期" width="200" style="height: 10px;">
                <template slot-scope="scope">
                    <i class="el-icon-time"></i>
                    <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="设备类型" width="120">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备类型: {{ scope.row.type }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.type }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="设备编号" width="300">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备ID: {{ scope.row.deviceId }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type='success'>{{ scope.row.deviceId }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="设备状态" width="120">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备状态: {{ scope.row.status==true?'在线':'离线' }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type='danger'>{{  scope.row.status==true?'在线':'离线' }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" @click="controlDevice(scope.$index, scope.row)">控制</el-button>
                    <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="primary" round style="display: block;width: 200px; margin: 30px auto;" @click='addDevice'>增加设备
        </el-button>


        <el-dialog title="添加设备信息" :visible.sync="addDialogVisible" width="30%">
            <el-form ref="addForm" :model="addForm" label-width="80px">
                <el-form-item label="设备id" prop="deviceId" required>
                    <el-input v-model="addForm.deviceId"></el-input>
                </el-form-item>
                <el-form-item label="设备密码" prop="deviceKey" required>
                    <el-input v-model="addForm.deviceKey"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="addSubmit()">提交</el-button>
                    <el-button @click="addDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="更新设备信息" :visible.sync="updateDialogVisible" width="30%">
            <el-form ref="updateForm" :model="updateForm" label-width="80px">
                <el-form-item label="当前设备id" prop="oldDeviceId" required>
                    <el-input v-model="updateForm.oldDeviceId" readonly="readonly" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="当前设备密码" prop="oldDeviceKey" required>
                    <el-input v-model="updateForm.oldDeviceKey"></el-input>
                </el-form-item>
                <el-form-item label="新设备id" prop="newDeviceId" required>
                    <el-input v-model="updateForm.newDeviceId"></el-input>
                </el-form-item>
                <el-form-item label="新设备密码" prop="newDeviceKey" required>
                    <el-input v-model="updateForm.newDeviceKey"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="updateSubmit()">提交</el-button>
                    <el-button @click="updateDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <chatWindow ref="chatWindow" />
    </div>

</template>

<script>
    import { formatDate } from '../common/dateUtil.js'
    import chatWindow from "./chatWindow.vue"
    import qs from 'qs';
    export default {
        name: 'device',
        mounted() {
            this.initData()
        },
        data() {
            return {
                devices: [],
                addDialogVisible: false,
                updateDialogVisible: false,
                submitType: "",
                addForm: {
                    deviceId: "",
                    deviceKey: ""
                },
                updateForm: {
                    oldDeviceId: "",
                    oldDeviceKey: "",
                    newDeviceId: "",
                    newDeviceKey: ""
                },
            }
        },
        components: {
            chatWindow
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
                this.updateForm.oldDeviceId = this.devices[index]['deviceId'];
                this.updateDialogVisible = true;
            },
            updateSubmit() {
                if (this.updateForm.oldDeviceId == this.updateForm.newDeviceId) {
                    this.$message.error('请保证当前设备id与更新id不同！');
                    return
                }
                this.$confirm('确定要将设备id改为：' + this.updateForm.newDeviceId + '吗？', '更新设备编号', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log(JSON.stringify(this.updateForm))
                    this.$axios.post('device/update',
                        qs.stringify(
                            this.updateForm
                        )).then(res => {
                            if (res.data.code == 200) {
                                this.$message({
                                    type: 'success',
                                    message: res.data.msg
                                });
                                this.updateDialogVisible = false;
                                this.updateForm['olddeviceId'] = '';
                                this.updateForm['olddeviceKey'] = '';
                                this.updateForm['newdeviceId'] = '';
                                this.updateForm['newdeviceId'] = '';
                                this.initData();
                            } else {
                                this.$message.error(res.data.msg);
                            }
                        })
                }).catch((err) => {
                });
            },
            addDevice() {

                this.addDialogVisible = true;
            },
           
            addSubmit() {

                this.$axios.post('device/add', qs.stringify(this.addForm)).then(res => {
                    if (res.data.code == 200) {
                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });
                        this.addDialogVisible = false;
                        this.addForm['deviceId'] = '';
                        this.addForm['deviceKey'] = '';
                        this.initData();
                    } else {
                        this.$message.error(res.data.msg);
                    }
                })

            },
            controlDevice(index, row) {
                this.$refs.chatWindow.currDeviceId = this.devices[index].deviceId
                this.$refs.chatWindow.roobotWindow = true;
            },

            handleDelete(index, row) {
                console.log(index, row);
                this.$prompt('请输入该设备的密码', '输入密码', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(({ value }) => {
                    if (value && value != '') {
                        this.$confirm('确定要从您的账户下移除该设备吗？', '删除设备', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.$axios.post('device/delete', qs.stringify({ "deviceId": this.devices[index]['deviceId'], "deviceKey": value })).then(res => {
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
                    }
                })

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