<template>
  <div style="height:auto">
    <div class="head">
      <div>
        <ul>
          <li><a @click='moveTo(".app")'>首页</a></li>
          <li><a @click='moveTo("#td")'>产品特点</a></li>
          <li><a @click='moveTo("#bz")'>使用方法</a></li>
          <!-- <li><a href="#ln">关于我们</a></li> -->
          <li>
            <router-link to="/login">登录后台</router-link>
          </li>
        </ul>
      </div>
    </div>
    <img src="../assets/homebg.png" alt="" style="width:100%;position: absolute;z-index: -1;">
    <div class="app">
      <div class="app_left an_left">
        <h1>合肥学院毕业设计</h1>
        <h1>基于语音识别的远程控制系统</h1>
        <h1>Design By 16自动化二班雷文珲</h1>
        <div>
          <a href="#" class='app_left_btn btn_primary' @click="downAPP">下载APP</a>
          <a href="#" class='app_left_btn btn_danger' @click="buy">购买产品</a>
        </div>
      </div>
      <div class="app_right an_right">
        <img src="../assets/app.png" alt="" class="iphone">
        <span class='small-round attr_red an_right'>语音交互</span>
        <span class='small-round attr_blue an_left'>智能聊天</span>
        <span class='small-round attr_yellow an_right'>精准操控</span>
        <span class='small-round attr_green an_left'>多元素化</span>
      </div>
    </div>
    <div>
      <div id='td' class="first_content ">
        <h1><i class="fas fa-clock"></i></font-awesome-icon><span style="margin-left: 10px;">产品特点</span></h1>
        <div class="box an_top">
          <div class="card">
            <div style="font-size: 60px;margin-right: 20px;">
              <!-- <i class="far fa-gem"></i> -->
              <font-awesome-icon :icon="['far', 'gem']"></font-awesome-icon>
            </div>
            <div>
              <h2>语音控制</h2>
              <span>您只需要开口说话，就可以让轻易操控设备。对于那些行动不便的人来讲，使用本产品再好不过了。</span>
            </div>
          </div>
          <div class="card">
            <div style="font-size: 60px;margin-right: 20px;">
              <!-- <i class="far fa-gem"></i> -->
              <font-awesome-icon :icon="['fas', 'book-open']"></font-awesome-icon>
            </div>
            <div>
              <h2>无欢不乐</h2>
              <span>本产品可以和孩子（4-6）岁进行实时“聊天”，培养孩子的思考方式。曼拉可以讲故事、天气播报哦~</span>
            </div>
          </div>
          <div class="card">
            <div style="font-size: 60px;margin-right: 20px;">
              <i class="fas fa-child"></i>
            </div>
            <div>
              <h2>智能衔接</h2>
              <span>针对开发者，本产品可提供在线接口供二次开发，轻松衔接您的智能家居产品。</span>
            </div>
          </div>
        </div>
      </div>
      <div id='bz' class="second_content an_top">

        <h1><i class="fas fa-hockey-puck"></i><span style="margin-left: 10px;">使用步骤</span></h1>
        <div style="margin-top: 100px;">
          <el-steps :active="active" align-center>
            <el-step title="步骤1" description="淘宝或者官网购买商品，隔日达第二天即可收到产品哦"></el-step>
            <el-step title="步骤2" description="注册账号，并绑定设备id到您的账号下。"></el-step>
            <el-step title="步骤3" description="下载我们产品的专属APP,登录您的账号"></el-step>
            <el-step title="步骤4" description="启动硬件设备，连接好产品与设备，完工，开启您的语音控制把~"></el-step>
          </el-steps>
        </div>
        <el-button style="margin-top: 12px;width:150px;display: block; margin: 50px auto;" @click="next">下一步</el-button>
      </div>
      <div class="thirty_content an_top">
        <!-- <h1 id='ln'><i class="fas fa-clock"></i></font-awesome-icon>关于我们</h1>
        -->
      </div>
    </div>
    <el-dialog title="购买产品" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <el-form ref="ruleForm" :rules="rules" :model="ruleForm" label-width="80px">
        <el-form-item label="您的姓名" prop="name" required>
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="收获地址" prop="address" required>
          <el-input v-model="ruleForm.address"></el-input>
        </el-form-item>
        <el-form-item label="选择版本" prop="version" required>
          <el-select v-model="ruleForm.version" placeholder="请选择版本">
            <el-option label="普通版（￥128）" value="normar"></el-option>
            <el-option label="至尊版（￥288）" value="plus"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="配送时间" prop="time" required>
          <el-col :span="11">
            <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.time" style="width: 100%;">
            </el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-time-picker placeholder="选择时间" v-model="ruleForm.date2" style="width: 100%;"></el-time-picker>
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit('ruleForm')">立即购买</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>
  </div>

</template>

<script>
  import scrollReveal from 'scrollreveal';

  export default {
    name: 'index',
    mounted() {
      window.addEventListener('scroll', this.handleScroll, true);
      this.scrollReveal.reveal('.an_left', {
        // 动画的时长
        duration: 800,
        // 延迟时间
        delay: 200,
        // 动画开始的位置，'bottom', 'left', 'top', 'right'
        origin: 'left',
        // 回滚的时候是否再次触发动画
        reset: true,
        // 在移动端是否使用动画
        mobile: false,
        // 滚动的距离，单位可以用%，rem等
        distance: '200px',
        // 其他可用的动画效果
        opacity: 0.001,
        easing: 'linear',
        scale: 1.2,
        viewportFactor: 0.33,
        rotate: {
          x: 20,
          z: 20
        }
      });
      this.scrollReveal.reveal('.an_right', {
        // 动画的时长
        duration: 500,
        // 延迟时间
        delay: 200,
        // 动画开始的位置，'bottom', 'left', 'top', 'right'
        origin: 'right',
        // 回滚的时候是否再次触发动画
        reset: true,
        // 在移动端是否使用动画
        mobile: false,
        // 滚动的距离，单位可以用%，rem等
        distance: '200px',
        // 其他可用的动画效果
        opacity: 0.001,
        easing: 'linear',
        scale: 0.9,
      });
      this.scrollReveal.reveal('.an_top', {
        // 动画的时长
        duration: 1000,
        // 延迟时间
        delay: 200,
        // 动画开始的位置，'bottom', 'left', 'top', 'right'
        origin: 'bottom',
        // 回滚的时候是否再次触发动画
        reset: true,
        // 在移动端是否使用动画
        mobile: true,
        // 滚动的距离，单位可以用%，rem等
        distance: '50px',
        // 其他可用的动画效果
        opacity: 0.001,
        easing: 'linear',
        scale: 0.9,

      });
    },
    data() {
      return {
        scrollReveal: scrollReveal(),
        active: 0,
        dialogVisible: false,
        ruleForm: {
          name: '',
          address: '',
          version: '',
          time: ''
        },
        rules: {
          name: [
            { required: true, message: '请输入您的名字', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '请输入您的收货地址', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          version: [
            { required: true, message: '请选择您需要的版本', trigger: 'blur' },
          ],
          time: [
            { required: true, message: '请选择您的配送时间', trigger: 'blur' },
          ]
        }
      }
    },
    methods: {
      buy() {
        this.dialogVisible = true
      },
      moveTo(id) {
        // 平滑过渡
        document.querySelector(id).scrollIntoView({
          behavior: "smooth"
        });
      },
      downAPP() {
        this.$message({
          message: 'APP正在优化中，稍后几天即可下载哦~',
          type: 'warning'
        });
      },
      onSubmit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.log('error submit!!');
            return false;
          }
        })
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => { });
      },
      next() {
        if (this.active++ >= 4) this.active = 0;
      },
      handleScroll() {
        var heigth = document.documentElement.scrollTop;
        var headDom = document.getElementsByClassName("head")[0];
        var ls = document.getElementsByClassName("head")[0].childNodes[0].childNodes[0].children;

        if (heigth >= 100) {
          headDom.style.backgroundColor = "whitesmoke";
          headDom.style.borderBottom = '2px solid #e0e0e0';
          for (let index = 0; index < ls.length; index++) {
            ls[index].childNodes[0].style.color = "#6F6F6F";
          }
        }
        if (heigth == 0) {
          headDom.style.backgroundColor = "transparent";
          headDom.style.borderBottom = '0';
          for (let index = 0; index < ls.length; index++) {
            ls[index].childNodes[0].style.color = "white";
          }
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  .head {
    text-align: center;
    background-color: transparent;
    width: 100%;
    position: fixed;
    color: whitesmoke;
    font-size: 16px;
    font-weight: bold;
    z-index: 10;

  }

  .head ul::before {
    content: url("../assets/ico.png");
    position: relative;
    left: -100px;
    top: 10px;
  }

  .head ul {
    display: flex;
    flex-direction: row;
    width: 60%;
    justify-content: center;
    margin-left: 150px;
    /* background-color: tomato; */
  }

  .head ul li {
    width: 25%;
    padding: 30px 10px 00px;
    /* background-color:thistle; */
    margin: 0 10px;
    height: 40px;
  }

  .head ul li a {
    color: aliceblue;
    display: inline-block;
    width: 100%;
    height: 100%;
    cursor: pointer;
  }

  .app {
    height: 500px;
    display: flex;
    width: 1200px;
    padding-top: 200px;
    display: flex;
    flex-wrap: wrap;
    justify-self: center;
    user-select: none;
  }

  .app_left {
    width: 60%;

  }

  .app_left h1 {
    position: relative;
    left: 60px;
    font-size: 40px;
    color: white;
    text-align: center;
    font-weight: 700;
    line-height: 4.25rem;
    font-family: 'Ubuntu sans-serif';

  }

  .app_left_btn {
    color: white;
    font-size: 20px;
    display: inline-block;
    width: 200px;
    padding: 20px 0;
    text-align: center;
    border-radius: 10px;
    position: relative;
    left: 150px;
    top: 50px;
    margin-left: 50px;
  }

  .btn_primary {
    background-color: #fc6a42;
  }

  .btn_danger {
    background-color: #4567c9;
  }

  .app_right {
    width: 40%;
  }

  .iphone {
    position: relative;
    width: 400px;
    height: 500px;
    left: 40%;
    top: -50px;
  }

  .attr_red {
    width: 90px;
    height: 90px;
    top: 55%;
    left: 30%;
    color: white;
    background-color: #fc6a42;
  }

  .attr_blue {
    width: 90px;
    height: 90px;
    top: 50%;
    left: 112%;
    color: white;
    background-color: #4466c9;
  }

  .attr_yellow {
    width: 90px;
    height: 90px;
    top: -5%;
    left: 35%;
    background-color: #fcec64;
    color: #797979;
  }

  .attr_green {
    width: 90px;
    height: 90px;
    top: -5%;
    left: 115%;
    background-color: springgreen;
    color: rgb(82, 6, 145);
  }

  .small-round {
    display: block;
    border-radius: 100%;
    position: absolute;
    text-align: center;
    line-height: 90px;
  }

  .first_content,
  .second_content,
  .thirty_content {
    padding-top: 100px;

  }

  .first_content h1,
  .second_content h1,
  .thirty_content h1 {
    text-align: center;
    color: #648cff;
  }

  .box {
    display: flex;
    justify-content: space-between;
    width: 80%;
    margin: 100px auto 10px;
  }

  .card {
    display: flex;
    width: 20%;
    justify-content: space-around;

  }
</style>