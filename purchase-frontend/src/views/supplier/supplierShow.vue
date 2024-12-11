<template>
  <div class="indexAll" >

    <el-row :gutter="20">

      <el-col :span="auto" class="el-col_14" >
        <div class="grid-content meddie">
          <!-- 可视化大屏 中间 -->
          <Meddie></Meddie>
        </div>
      </el-col>


    </el-row>
  </div>
</template>

<script>

import Meddie from '@/views/supplier/supplierScreen'
export default {
  name:'index',
  data() {
    return {

    }
  },
  components:{
    //Left,
    Meddie,
    //Right
  }
}
</script>
<style lang='less' scoped>
.indexAll {
  background-color: #080a20;
  width: 1000vh;
}

.meddie{
  width: 10vh;
  //height: 100vh;
margin-right: -100px;
  margin-left: 125px;
}
</style>
