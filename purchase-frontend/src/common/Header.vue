<template>
  <!-- 最上边 -->
  <div class="header-container" >

    <div class="l-content" @contextmenu.prevent="rightClick">
      <el-button icon="el-icon-menu" size="medium" style="margin-left: 10px" @click="intoFont" >进入前台</el-button>
      <!-- 菜单收缩的图 button绑定一个点击事件（方法） -->
      <el-button icon="el-icon-menu" size="medium" style="margin-left: 15px" @click="handleMenu"></el-button>
      <!--  收缩图右边----的面包屑 -->
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-left:20px;" >
        <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ path: item.path }" >
          {{ item.label }}
        </el-breadcrumb-item>
      </el-breadcrumb>
      <!--进入前台-->
    </div>


    <!-- 右侧头像 -->
    <div class="r-content">
      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">
           <img class="user_img" src="@/assets/wallhaven-p97wgp.png" />
        </span>
        <el-dropdown-menu slot="dropdown">
          <!-- <el-dropdown-item command="c">个人信息</el-dropdown-item> -->
          <el-dropdown-item command="a">修改密码</el-dropdown-item>
          <el-dropdown-item command="b">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- Form -->
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%" @close="closeDialog()">
      <el-form :model="updatePassword" :rules="passwordRules" ref="passwordForm" label-width="100px"
               class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input type="password" v-model="updatePassword.oldPassword" placeholder="请输入原密码" clearable></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="updatePassword.newPassword" placeholder="请输入新密码" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmitFormData('passwordForm')">确定</el-button>
          <el-button @click="dialogFormVisible = false">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- <el-dialog>
      <el-descriptions title="用户信息">
    <el-descriptions-item label="用户名">kooriookami</el-descriptions-item>
    <el-descriptions-item label="手机号">18100000000</el-descriptions-item>
    <el-descriptions-item label="居住地">苏州市</el-descriptions-item>
    <el-descriptions-item label="备注">
      <el-tag size="small">学校</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="联系地址">江苏省苏州市吴中区吴中大道 1188 号</el-descriptions-item>
</el-descriptions>
    </el-dialog> -->

  </div>
</template>
<script>

import { mapState } from 'vuex'

export default {
  name: "Header",
  data() {
    //旧密码必须写校验
    var checkPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      }
      else{
        // console.log("密码",this.updatePassword)
        //对输入的旧密码进行后端校验
        this.$http.post("/admin/checkPassword", this.updatePassword)
            .then((res) => {
              console.log("校验-返回", res.data);
              //说明输入的旧密码错误
              if (res.data.data == 'false') {
                callback(new Error('旧密码输入错误'));
              }else{
                callback();  //都需要回调
              }
            }).catch(console.error());
      }
    }
    return {

      //定义密码
      dialogFormVisible: false,
      // dialogDescriptions:false,
      updatePassword: {
        username: sessionStorage.getItem('username'),
        oldPassword: "",
        newPassword: ""
      },
      passwordRules: {
        oldPassword: [{ validator: checkPassword, trigger: "blur" }],
        newPassword: [{ required: true, message: "请输入新密码", trigger: "blur" }]
      }
    };
  },
  methods: {
    intoFont () {
      this.$router.push("/vr")
    },
    rightClick () {
      // console.log("鼠标右击事件")
    },
    // 对菜单做缩放操作  collapseChange() js是在 tab.js中
    handleMenu() {
      // console.log("触发按钮");

      // 通过 store.state 来获取状态对象，并通过 store.commit 方法触发状态变更
      // Vue 组件中， 可以通过 this.$store  commit('xx')调用方法名为collapseChange 在Aside.vue同步获取isxx()
      // ------------------------全局使用的store
      // console.log("Header中路由" + this.$store);
      this.$store.commit('collapseChange');
    },
    // 右侧  //a 修改密码 b 退出
    handleCommand(commond) {

      //个人信息
      if ('a' === commond) {
        this.dialogFormVisible = true;
      }
      if ('b' === commond) {
        this.$confirm('是否确认注销登录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 表示处理的是 注销操作
          //  sessionStorage.clear(); // 清空存储的Token信息
          // 然后跳转到登录页
          this.$router.push("/login")
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消注销'
          });
        });
      }
      //个人信息
      // if('c' === commond){
      //   this.dialogDescriptions =true;
      // }
    },
    //修改密码提交
    handleSubmitFormData(passwordForm) {
      this.updatePasswordParam(passwordForm);
    },
    updatePasswordParam(passwordForm) {
      //表单验证通过则
      this.$refs[passwordForm].validate((valid) => {
        if (valid) {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;
          //对输入的密码进行后端校验
          this.$http.post("/admin/updatePassword", this.updatePassword)
              .then((res => {
                console.log("更新密码", res);
                this.dialogFormVisible = false; //关闭
                //清空表单
                this.updatePassword = {
                  oldPassword: "",
                  newPassword: ""
                }
                this.dialogFormSubmitVisible = false;
                this.$message({
                  type: 'info',
                  message: '修改成功'
                })
              })).catch(() => { })
        }
      })

    },
    //关闭窗口就清空数据
    closeDialog() {
      this.updatePassword = {
        oldPassword: "",
        newPassword: ""
      }
    }
  },
  computed: {
    // 使用对象展开运算符将此对象混入到外部对象中
    //tab是新加的
    // 获取不了tags：
    ...mapState({
      tags: state => state.tab.tabsList
    })
  }
};
</script>
<style lang="less" scoped>
.header-container {
  height: 60px;
  background-color: rgb(62,114,144);
  display: flex;
  justify-content: space-between;
  align-items: center;

  .index {
    color: #fff;
    font-size: 16px;
    margin-left: 10px;
    cursor: pointer;
  }

  .r-content {
    padding-right: 20px;

    .user_img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
    }
  }
  .r-content2 {
    padding-left: -5px;
  }

  .l-content {
    display: flex;
    align-items: center;

    /deep/.el-breadcrumb__item {
      .el-breadcrumb__inner {
        font-weight: normal;

        &.is-link {
          color: #fff;
        }
      }

      &:last-child {
        .el-breadcrumb__inner {
          color: #fff;
        }
      }

    }
  }
  .welcome{
    font-size: 12px;
    margin-left: 70%;
    color: #fff;
  }
}
</style>

