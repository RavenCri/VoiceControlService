<template>
  <div id="app">
    <div class="head">
        <div>
          <ul>
            <li><a href="#">首页</a></li>
            <li><a href="#">时间安排</a></li>
            <li><a href="#">设备管理</a></li>
            <li><a href="#">我们的理念</a></li>
          </ul>
        </div>
    </div>
    <div >
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>

*{
  padding: 0px;
  margin: 0px;
  list-style: none;
  border: none;
  text-decoration: none;
}
.head{
  text-align: center;
  background-color: transparent;
  width: 100%;
  position: fixed;
  color: whitesmoke;
  font-size: 16px;
  font-weight: bold;
  z-index: 10;

}
.head ul::before{
  content:url("./assets/ico.png");
  position: relative;
  left: -100px;
  top:10px;
}
.head ul{
  display: flex;
  flex-direction: row;
  width: 60%;
  justify-content: center;
  margin-left: 150px;
  /* background-color: tomato; */
}
.head ul li{
  width: 25%;
  padding: 30px 10px 00px ;
  /* background-color:thistle; */
  margin: 0 10px;
  height: 40px;
}
.head ul li a{
  color: aliceblue;
  display: inline-block;
  width: 100%;
  height: 100%;
}
</style>
