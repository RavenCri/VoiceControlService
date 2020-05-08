<template>
    <div>
        <div class="title"><i class="el-icon-position"></i>出厂设备列表</div>
        <el-divider></el-divider>
        <el-table :data="devices" style="width: 100%" :border=true :stripe=true :highlight-current-row=true>
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
                            <el-tag size="medium" v-if="scope.row.type=='normal'">普通版</el-tag>
                            <el-tag size="medium" v-if="scope.row.type=='plus'">增强版</el-tag>
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
            <el-table-column label="设备绑定用户组" width="120">
                <template slot-scope="scope">
                    <el-tag size="medium" type='danger' style="margin-bottom: 10px;" v-if="scope.row.users.length==0">无
                    </el-tag>
                    <el-popover trigger="hover" placement="top">
                       <p>绑定用户组</p>
                        <div slot="reference" class="name-wrapper" v-for="(item,index) in scope.row.users">
                            
                            <el-tag size="medium" type='warning' style="margin-bottom: 10px;">{{ item.nickname }}
                            </el-tag>
                        </div>

                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="设备状态" width="120">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>设备状态: {{ scope.row.status==true?'在线':'离线' }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type='danger' v-if="scope.row.status==true">在线</el-tag>
                            <el-tag size="medium" type='success' v-if="scope.row.status==false">离线</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">

                    <!-- <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button> -->
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.deviceId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="primary" round style="display: block;width: 200px; margin: 30px auto;" @click='addDevice'>
            增加设备
        </el-button>


        <el-dialog title="出厂设备信息" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
            <el-form ref="ruleForm" :rules="rules" :model="ruleForm" label-width="80px">
                <el-form-item label="设备id" prop="deviceId" required>
                    <el-input v-model="ruleForm.deviceId"></el-input>
                </el-form-item>
                <el-form-item label="选择版本" prop="type" required>
                    <el-select v-model="ruleForm.type" placeholder="请选择版本">
                        <el-option label="普通版（￥128）" value="normal"></el-option>
                        <el-option label="至尊版（￥288）" value="plus"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="生产日期" prop="time" required>
                    <el-col :span="11">
                        <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.time" style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                    <el-col class="line" :span="2">-</el-col>
                    <el-col :span="11">
                        <el-time-picker placeholder="选择时间" v-model="ruleForm.date2" style="width: 100%;">
                        </el-time-picker>
                    </el-col>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit('ruleForm')">提交</el-button>
                    <el-button @click="dialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs';
    export default {
        name: 'deviceStatus',
        data() {
            return {
                devices: [],
                ruleForm: {
                    deviceId: "",
                    version: "",
                    time: ""
                },
                dialogVisible: false,
                rules: {
                    id: [
                        { required: true, message: '请输入产品id', trigger: 'blur' },
                        { min: 19, max: 19, message: '长度为19个字符', trigger: 'blur' }
                    ],
                    type: [

                        { min: 3, max: 5, message: '请选择您需要的版本', trigger: 'blur' }
                    ],
                    time: [
                        { required: true, message: '请输入设备出厂时间', trigger: 'blur' },
                    ],

                }
            }
        },
        mounted() {
            this.initData()
        },
        methods: {
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => { });
            },
            initData() {
                this.$axios.get('devicePlus/list').then(res => {
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
            onSubmit() {
                this.$axios.post('devicePlus/addDevice', qs.stringify(this.ruleForm)).then(res => {
                    this.dialogVisible = false;
                    if (res.data.code == -1) {
                        this.$alert('您的token已失效，请重新登录！', '数据异常', {
                            confirmButtonText: '确定',
                            callback: action => {
                                localStorage.clear('token');
                                this.$router.push({ name: 'login' })
                            }
                        });
                        
                    } else if (res.data.code == 200) {
                        this.$message.success(res.data.msg)
                        this.initData()
                    }

                })
            },
            handleEdit(index, row) {

            },
            handleDelete(deviceId) {
                this.$confirm('该操作将会删除所有绑定此设备的用户记录，您确定要删除该设备吗？', '删除设备', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post('devicePlus/deleteDevice', qs.stringify({
                        "deviceId": deviceId
                    })).then(res => {
                        this.dialogVisible = false;
                        if (res.data.code == -1) {
                            this.$alert('您的token已失效，请重新登录！', '数据异常', {
                                confirmButtonText: '确定',
                                callback: action => {
                                    localStorage.clear('token');
                                    this.$router.push({ name: 'login' })
                                }
                            });
                            
                        } else if (res.data.code == 200) {
                            this.$message.success(res.data.msg)
                            this.initData()
                        }

                    })
                }).catch((err) => {
                });

            },
            addDevice() {
                this.dialogVisible = true;
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