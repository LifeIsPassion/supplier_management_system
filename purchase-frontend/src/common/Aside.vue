<template>
  <!-- 左侧菜单路由 -->
  <div class="asideContianer">
    <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
      :collapse="isCollapse" background-color="rgb(50, 65, 87)" text-color="#fff" active-text-color="#ffd04b">

      <h3 style="color: #666fd2">{{ isCollapse ? '商品': '供应商自助管理系统' }}</h3>

      <!--   一级菜单
         clickMenu点击菜单事件   noChildren 只有一级菜单 -->
      <el-menu-item v-for="item in noChildren" :key="item.name" :index="item.name" @click="clickMenu(item)">
        <i :class="`el-icon-${item.icon}`"></i> <!-- 取图标 -->
        <span slot="title">{{ item.label }}</span> <!-- 取名称 -->
      </el-menu-item>

      <!--有二级菜单-->
      <!-- 过滤出的含有 二级菜单给 item对象 这里是二级菜单总名 -->
      <!--分别展示具体二级菜单名  -->
      <!--hasChildren 返回一个 item -->
      <el-submenu v-for="item in hasChildren" :key="item.name" :index="item.name">
        <template slot="title">
          <i :class="`el-icon-${item.icon}`"></i>
          <span slot="title">{{ item.label }}</span>
        </template>
        <el-menu-item-group  style="margin-left:40px" v-for="subItem in item.children" :key="subItem.name">
          <el-menu-item :index="subItem.name" @click="clickMenu(subItem)">{{ subItem.label }}</el-menu-item>
        </el-menu-item-group>
      </el-submenu>


    </el-menu>
  </div>
</template>
<script>
export default{
  name: "Aside",
  data() {
    return {
      menuData:[],
    };
  },
  methods: {

    InitMenu() {    //获取菜单
      this.menuData=[];
      //不同用户展示不同的菜单
      this.$http.get("/menu/getShowMenu").then((res) =>{
        //获取当前用户的菜单信息
        this.menuData=res.data.data;
        // console.log("菜单打印：",this.menuData);
      });
    },

    handleOpen(key, keyPath) { },
    handleClose(key, keyPath) {},

    clickMenu(item) {   //点击某个菜单
      // 如果需要更新的路由和当前路由不一致就更新
      // console.log("Aside.vue-- 路由" + this.$route.path);
      if (this.$route.path != item.path && !(this.$route.path === '/index' && item.path === '/')) {
        this.$router.push(item.path)
      }
      // 触发面包屑数据的更新
      this.$store.commit('menuChange', item)
    },
  },

    //页面动态加载  完成当前用户的菜单加载
   mounted() {
     this.InitMenu();//初始化菜单
  },

   computed: {
    noChildren() { // 无子菜单
       return this.menuData.filter((item) => !item.children);
    },
    hasChildren() { // 有子菜单
      return this.menuData.filter((item) => item.children);
    },

    isCollapse() {   //状态管理 同步给Header.vue中的
      // console.log("展开\关闭-isCollapse:" + this.$store.state.tab.isCollapse);
      return this.$store.state.tab.isCollapse;
    }
  },


};
</script>


<style lang="less" scoped>
//   height: 100vh;
.el-menu {
  height: 100vh;
  border-right: none;

  h3 {
    color: #fff;
    text-align: center;
    line-height: 48px;
    font-size: 22px;
    font-weight: 400px;
    padding: 0 20px;
  }
}
</style>


