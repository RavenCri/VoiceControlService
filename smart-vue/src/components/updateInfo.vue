<template>
    <div style="width: 30%;margin: 0 auto">
        <p style="text-align: center;font-size: 25px;"><i class="el-icon-lock" style="font-size: 50px;"></i>修改密码</p>
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="当前密码" prop="currpass">
                <el-input type="password" v-model="ruleForm.currentPassword" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="password1">
                <el-input type="password" v-model="ruleForm.password1" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="password2">
                <el-input type="password" v-model="ruleForm.password2" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import qs from 'qs';
    import {updateUserInfo} from '@/common/api/user'
    export default {
        name: 'updateInfo',
        data() {
            var validatePass1 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入您当前的密码'));
                } else {
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入新密码'));
                } else if (value === this.ruleForm.currentPassword || value === this.ruleForm.currentPassword) {
                    callback(new Error('当前密码与新密码不能重复'));

                } else {
                    if (this.ruleForm.password2 !== '') {
                        this.$refs.ruleForm.validateField('password2');
                    }
                    callback();
                }
            };
            var validatePass3 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入确认密码'));
                } else if (value !== this.ruleForm.password1) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                ruleForm: {
                    currentPassword: '',
                    password1: '',
                    password2: ''
                },
                rules: {
                    currentPassword: [
                        { validator: validatePass1, trigger: 'blur' }
                    ],
                    password1: [
                        { validator: validatePass2, trigger: 'blur' }
                    ],
                    password2: [
                        { validator: validatePass3, trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$confirm('确定要修改密码吗', '修改密码', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            updateUserInfo(this.ruleForm).then(res=>{
                                if (res.data.code == 200) {
                                        this.$message({
                                            type: 'success',
                                            message: res.data.msg
                                        });
                                    }
                            }).catch(err=>{})
                            // this.$axios.post('account/update',
                            //     qs.stringify(this.ruleForm)).then(res => {
                            //         if (res.data.code == 200) {
                            //             this.$message({
                            //                 type: 'success',
                            //                 message: res.data.msg
                            //             });
                            //             this.initData();
                            //         } else {
                            //             this.$message.error(res.data.msg);
                            //         }
                            //     })
                        }).catch(err=>{})
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }

    }
</script>

<style scoped>

</style>