<template>
  <!-- 面包屑 -->
  <div class="tabs"  >
    <el-tag
      v-for="(item,index ) in tags"
      :key="item.path"
      :effect="$route.name === item.name ? 'dark' : 'plain'"
      @click="changeMenu(item)"
      :closable="item.name !== 'Index'"
      @close="handleClose(item,index)"
    >
      {{ item.label }}
    </el-tag>
  </div>
 
   
</template>
<script>
import { mapState, mapMutations } from "vuex";
export default {
    name: "CommonTab",
    data() {
        return {
        };
    },
    computed: {
        ...mapState({
            // tags: (state) => tab.state.tabsList,   //sate与tab兑换位置  --vue2 只能使用 vuex3
            tags: (state) => state.tab.tabsList,
        }),
    },
    methods: {
        ...mapMutations(["closeTag"]),
        changeMenu(item) {
            // 点击tab标签我们需要动态的路由
            this.$router.push(item.path);
        },
        handleClose(item, index) {
            // 关闭菜单
            this.closeTag(item);
            // 动态的处理路由
            if (this.$route.name !== item.name) {
                return;
            }
            // console.log("打印" + this.tags[0]);
            this.$router.push(this.tags[0]);
        },
    },
 
};
</script>
<style lang="less" scoped>
.tabs {
  position: relative;
  //height: 30px;
  overflow: hidden;
  background: #fff;
  //padding-right: 120px;
  box-shadow: 0 5px 10px #ddd;
  padding-bottom: 5px;
  padding-top: 5px;
  padding-left: 10px;
  .el-tag {
    margin-right: 15px;
    cursor: pointer;
  }
}
</style>