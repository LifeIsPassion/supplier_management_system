<template>
    <div class="indexAll" >

        <el-row :gutter="20" >
            <el-col :span="6" class="el-col_6" >
                <div class="grid-content  left">
                    <!-- 可视化大屏左侧 -->
                    <Left></Left>
                </div>

            </el-col>
            <el-col :span="14" class="el-col_14">
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
    background-color: #080a20;
 //margin-left: 0%;
  //  margin-top: 0%;
//  height: auto;
 
} 
.left{
        padding-right: 0px;
        margin-right: -10px;
    }
    .meddie{
        margin-left: -10px;
    }
</style>