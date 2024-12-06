<template>
  <div class="hello">
    <el-container>
      <el-aside width="auto"  >
        <!-- 左侧菜单 -->
        <Aside></Aside>
      </el-aside>
      <el-container>
        <el-header style="border: none;">
          <!-- 顶部区域 -->
          <Header></Header>
        </el-header>
        <!-- 顶部的路由 -->
        <CommonTab></CommonTab>
       
          <el-main class="main" >
              <!-- 路由的出口 -->
              <router-view ></router-view>
          </el-main>
        
      </el-container>
    </el-container>
  </div>
</template>

  
<script>
 import Aside from '@/common/Aside.vue'  //菜单
 import Header from '@/common/Header.vue' //顶部
 import CommonTab from '@/common/CommonTab.vue' //顶部下方的tab状态栏
export default {
  name: "HelloWorld",
  data() {
    return {
      msg: "Welcome to Your Vue.js App",
    };
  },
    components: {
    Aside,
    Header,
    CommonTab
  }
}
 
</script>
  
<style lang="less" scoped>
.el-header {
  padding: 0;
}
.main{
  border-top: 12px;
 
}
</style>
  