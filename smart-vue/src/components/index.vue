<template>
  <div style="height:auto">
    <img src="../assets/homebg.png" alt="" style="width:100%;position: absolute;z-index: -1;">
    <div class="app">
      <div class="app_left an_left">
          <h1>合肥学院毕业设计</h1>
          <h1>基于语音识别的远程控制系统</h1>
          <h1> by 16自动化二班雷文珲</h1>
         <div>
          <a href="#" class='app_left_btn btn_primary'>下载APP</a>
          <a href="#" class='app_left_btn btn_danger'>购买产品</a>
         </div>
      </div>
      <div class="app_right an_right">
        <img src="../assets/app.png" alt=""  class="iphone">
        <span class='small-round attr_red'>语音交互</span>
        <span class='small-round attr_blue'>智能聊天</span>
        <span class='small-round attr_yellow'>精准操控</span>
        <span class='small-round attr_green'>多元素化</span>
      </div>
    </div>
    <div class="content ">
      <h1><i class="fas fa-clock"></i></font-awesome-icon>时间安排</h1>
      <div class="box an_top">
        <div class="card">
          <div style="font-size: 60px;margin-right: 20px;">
            <!-- <i class="far fa-gem"></i> -->
            <font-awesome-icon :icon="['far', 'gem']"></font-awesome-icon>
          </div>
          <div>
            <h2>2月份-3月份</h2>
            <span>对项目进行构思，查找资料，分析对比各种可能存在的问题。</span>
          </div>
        </div>
        <div class="card">
          <div style="font-size: 60px;margin-right: 20px;">
            <!-- <i class="far fa-gem"></i> -->
            <font-awesome-icon :icon="['fas', 'book-open']"></font-awesome-icon>
          </div>
          <div>
            <h2>3月份-5月份</h2>
            <span>陆续展开编码APP、前台用户操作网页、后台服务器、硬件开发的工作，并及时测试修复可能出现的bug</span>
          </div>
        </div>
        <div class="card">
          <div style="font-size: 60px;margin-right: 20px;">
            <i class="fas fa-child"></i>
          </div>
          <div>
            <h2>5月份-6月份</h2>
            <span>完善不足的地方，按照老师的指示，完善毕设，顺利毕业。</span>
          </div>
        </div>
      </div>
      
  </div>
  </div>
  
</template>

<script>
import scrollReveal from 'scrollreveal';

export default {
  name: 'index',
  mounted () {
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
    scale: 0.9,
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
    duration: 500,
    // 延迟时间
    delay: 200,
    // 动画开始的位置，'bottom', 'left', 'top', 'right'
    origin: 'top',
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
  },
  data () {
    return {
      scrollReveal: scrollReveal()
    }
  },
  methods: {
    handleScroll() {
        var heigth = document.documentElement.scrollTop;
        var headDom = document.getElementsByClassName("head")[0];
        var ls = document.getElementsByClassName("head")[0].childNodes[0].childNodes[0].children;
        
        if(heigth >= 100){
          headDom.style.backgroundColor ="whitesmoke";
          headDom.style.borderBottom= '2px solid #e0e0e0';
          for (let index = 0; index < ls.length; index++) {
              ls[index].childNodes[0].style.color="#6F6F6F";
          }
        }
        if(heigth == 0){ 
          headDom.style.backgroundColor ="transparent";
          headDom.style.borderBottom= '0';
          for (let index = 0; index < ls.length; index++) {
              ls[index].childNodes[0].style.color="white";
          }
        }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>

.app{
  height: 500px;
  display: flex;
  width: 1200px;
  padding-top: 200px;
  display: flex;
  flex-wrap:wrap;
  justify-self: center;
  user-select: none;
}
.app_left{
  width: 60%;
  
}
.app_left h1{
  position: relative;
  left: 60px;
  font-size: 40px;
  color: white;
  text-align: center;
  font-weight: 700;
  line-height: 4.25rem;
  font-family: 'Ubuntu sans-serif';
  
}
.app_left_btn{
  color: white;
  font-size: 20px;
  display: inline-block;
  width: 200px;
  padding: 20px 0;
  text-align: center;
  border-radius: 10px;
  position: relative;
  left: 150px;
  top:50px;
  margin-left: 50px;
}
.btn_primary{
  background-color: #fc6a42;
}
.btn_danger{
  background-color: #4567c9;
}
.app_right{
  width: 40%;
}
.iphone{
  position: relative;
  width: 400px;
  height: 500px;
  left: 50%;
  top:0px;  
}
.attr_red{
  width: 90px;
  height: 90px;
  top:60%;
  left: 40%;
  color: white;
  background-color: #fc6a42;
}
.attr_blue{
  width: 90px;
  height: 90px;
  top:50%;
  left: 120%;
  color: white;
  background-color: #4466c9;
}
.attr_yellow{
  width: 90px;
  height: 90px;
  top:0%;
  left: 45%;
  background-color: #fcec64;
  color: #797979;
}
.attr_green{
  width: 90px;
  height: 90px;
  top:-5%;
  left: 115%;
  background-color:springgreen;
  color:rgb(82, 6, 145);
}
.small-round{
  display:block;
  border-radius: 100%;
  position: absolute;
  text-align: center;
  line-height: 90px;
}
.content{
  padding-top: 100px;

}
.content h1{
  text-align: center;
  color: #648cff;
}
.box{
  display: flex;
  justify-content:space-between; 
  width: 80%;
  margin: 100px auto 10px;
}
.card{
  display: flex;
  width: 20%;
  justify-content: space-around;
  
}

</style>
