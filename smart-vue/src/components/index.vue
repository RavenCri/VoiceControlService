<template>
  <div >
    <img src="../assets/homebg.png" alt="" style="width:100%;">
    <img src="../assets/app.png" alt="" class="app">
  </div>
  
</template>

<script>
export default {
  name: 'index',
  mounted () {
    window.addEventListener('scroll', this.handleScroll, true)
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  methods: {
    handleScroll() {
        var heigth = document.documentElement.scrollTop;
        var headDom = document.getElementsByClassName("head")[0];
        var ls = document.getElementsByClassName("head")[0].childNodes[0].childNodes[0].children;
        headDom.style.animation="mymove  0.5s linear"
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
  position: relative;
  top: -600px;
  left: 900px;
  width: 400px;
}
@keyframes mymove{
  from{
    background-color: transparent;
    transform: scale(1.3);
  }to{
    background-color: whitesmoke;
  }
}
</style>
