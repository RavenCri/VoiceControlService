<template>
    <div >
        <p>这是device</p>
        <p>这是device</p><p>这是device</p><p>这是device</p><p>这是device</p><p>这是device</p><p>这是device</p>
    </div>
</template>

<script>
export default {
    name: 'device',
    mounted () {
        console.log('11')
        var headDom = document.getElementsByClassName("head")[0];
        var ls = document.getElementsByClassName("head")[0].childNodes[0].childNodes[0].children;
        headDom.style.backgroundColor = "whitesmoke";
        headDom.style.borderBottom = '2px solid #e0e0e0';
        for (let index = 0; index < ls.length; index++) {
        ls[index].childNodes[0].style.color = "#6F6F6F";
        }
    }

}

</script>

<style >

</style>