<template>
  <div class="container">


    <div style="width: 50%; display: flex;background-color: white; border-radius: 10px;">
      <div style="width: 50%; border-radius: 10px;" >
        <img src="../assets/fang.png" width="100%" height="100%" style="overflow: hidden;border-radius: 10px 0 0 10px">
      </div>
      <div style="flex: 1;width: 50%;padding: 50px;display: flex;flex-direction: column;justify-content: center;" >
        <div style="text-align: center; font-size: 26px; margin-bottom: 50px; color: #333">商品进销存管理系统</div>
        <el-form :model="form" :rules="rules" status-icon  ref="formName">
          <el-form-item  prop="username">
            <el-input v-model="form.username" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item  prop="password" >
            <el-input type="password" v-model="form.password" show-password placeholder="请输入密码"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" size="medium" style="width: 324px ;" @click="submitFormData">登录</el-button>
          </el-form-item>
        </el-form>
      </div>



    </div>

  </div>
</template>
<script>
export default {
  name: "Login",
  data() {
    return {
      form: {
        username: "",
        password: "",
      },
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },

    };
  }, methods: {
    // 提交登录表单的数据
    submitFormData() {
      // console.log(this.form);
      this.$refs['formName'].validate((valid) => {
        if (valid) {
          this.$http.post("/login", this.form)
            .then((res) => {
              // console.log(res);
              //获取服务端返回的信息
              if (res.data.code === 200) {
                // 表示登录成功
                // 1.存储相关的token信息  token信息在响应的header中
                sessionStorage.setItem("token", res.headers.authorization)
                sessionStorage.setItem("username", this.form.username)
                // 2.路由到主页面  --路由中配置了重定向
                this.$router.push("/")
              } else {
                // 表示登录失败，显示错误提示
                this.$message.error(res.data.msg+"或者新员工未分配角色");
              }
            });
        } else {
          return false;
        }
      });
    }
  }
}
</script>
<style lang="less" scoped>


.container {
  height: 100vh;
  overflow: hidden;
  //background-color: #2A60C9;
  background-image: url("../assets/jia.png");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
.login_container {
  width: 100%;
  height: 100vh;
  background-color: #1a77db;
  background-size: 100%;
  background-position: center bottom;
  background-repeat: no-repeat;

  border: none;
  border-radius: 0px;
  display: flex;
  justify-content: center;
  align-items: center;

  .login_form {
    opacity: 0.9;
    width: 400px;
    height: 400px;
    margin: 0px auto;
    padding: 0 55px 15px 35px;
    background-color: #fff;
    border: none;
    border-radius: 12px;
    // 添加阴影效果
    box-shadow: 0 0 25px #cac6c6;

    .login_title {
      font-family: "微软雅黑 Bold", "微软雅黑";
      font-weight: 700;
      text-decoration: none;
      color: rgb(0, 121, 254);
      font-size: 32px;
      margin-top: 50px;
      margin-bottom: 30px;
      margin-left: 43px;
      text-align: center;
    }
    .demo-ruleForm{
      margin-top: 70px;
      margin-bottom: 30px;
      margin-right: 50px;
    }

  }

}
</style>
