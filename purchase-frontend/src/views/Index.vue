<template>
    <div class="indexAll" >

        <el-row :gutter="20" >
            <el-col :span="8" class="el-col_6" >
                <div class="grid-content  left">
                    <!-- 可视化大屏左侧 -->
                    <Left></Left>
                </div>

            </el-col>
            <el-col :span="10" class="el-col_14">
                <div class="grid-content meddie">
                    <!-- 可视化大屏 中间 -->
                    <Meddie></Meddie>
                </div>
            </el-col>

            <el-col :span="4">
                <div class="grid-content right">

                    <Right></Right>
                </div>
            </el-col>

        </el-row>
    </div>
</template>

<script>


import Left from '@/views/screen/indexLeft.vue'
import Meddie from '@/views/screen/indexMeddie'
import Right from '@/views/screen/indexRight'
export default {
    name:'index',
    data() {
        return {

        }
    },
    components:{
        Left,
        Meddie,
        Right
    }
}
</script>
<style lang='less' scoped>
.indexAll {
    //  color: #ab2f2f;  //包括文字了
    //background-color: #080a20;
 margin-left: -25px;
  margin-right: -40px;
  margin-top: -20px;
  margin-bottom: -20px;
  //margin-bottom: -20px;
  //margin-top: 0%;
  //height: 85vh;
  //background-image: url('../assets/980.jpg');
  /*width: 1920px;*/
  /*height: 1080px;*/
  width: 100vw; /* 宽度设置为视口宽度的100% */
  height: 92vh; /* 高度设置为视口高度的100% */
  background-image: url('../assets/wallhaven-p97wgp.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
.left{
  padding-right: 1px;
  //margin-right: 10px;
  margin-left: 20px;
}
.meddie{
  //margin-left: 5px;
}
</style>
