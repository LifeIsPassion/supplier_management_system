<style>
.center {
  /*width: 1920px;*/
  /*height: 1080px;*/
  width: 100vw; /* 宽度设置为视口宽度的100% */
  height: 100vh; /* 高度设置为视口高度的100% */
  background-image: url('../assets/wallhaven-p97wgp.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

h1 {
  font-size: 30px;
  color: black;
}

.logon {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
  /* position: relative;
  overflow: hidden; */
  width: 768px;
  max-width: 100%;
  min-height: 480px;
  margin-top: 20px;
  display: flex;
  background: -webkit-linear-gradient(right, #4284db, #29eac4);
}

.overlaylong {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaylongleft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(100%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaylongright {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaytitle {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
}


.overlaytitleH2 {
  font-size: 30px;
  color: #fff;
  margin-top: 20px;
}

.overlaytitleP {
  font-size: 15px;
  color: #fff;
  margin-top: 20px;
}

.overlaytitleleft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
}

.overlaytitleright {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(-100%);
  transition: transform 0.6s ease-in-out;
}

.overlaytitle-Signin {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.overlaytitle-Signup {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.buttongohs {
  width: 180px;
  height: 40px;
  border-radius: 50px;
  border: 1px solid #fff;
  color: #fff;
  font-size: 15px;
  text-align: center;
  line-height: 40px;
  margin-top: 40px;
}

.overlaylongH2 {
  font-size: 25px;
  color: black;
  /* width: 250px; */
}

.overlaylong-Signin {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.overlaylong-Signup {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

input {
  background-color: #eee;
  border: none;
  padding: 12px 15px;
  margin: 10px 0;
  width: 240px;
}
h3{
  font-size: 10px;
  margin-top: 10px;
  cursor: pointer;
}
.inupbutton{
  background-color: #29eac4;
  border: none;
  width: 180px;
  height: 40px;
  border-radius: 50px;
  font-size: 15px;
  color: #fff;
  text-align: center;
  line-height: 40px;
  margin-top: 30px;
}
</style>

<template>
  <div class="center">
    <h1>供应商自助管理系统</h1>
    <div class="logon">
      <div :class="overlaylong">
        <div class="overlaylong-Signin" v-if="disfiex == 0">
          <h2 class="overlaylongH2">密码登录</h2>
          <el-form :model="form" :rules="rules" status-icon  ref="formName">
            <el-form-item  prop="username">
              <el-input v-model="form.username" placeholder="请输入账号"></el-input>
            </el-form-item>
            <el-form-item  prop="password" >
              <el-input type="password" v-model="form.password" show-password placeholder="请输入密码"></el-input>
            </el-form-item>
            <h3>忘记密码请联系管理员</h3>
            <el-form-item>
              <el-button type="primary" size="medium" style="width: 324px ;" @click="submitFormData">登录</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="overlaylong-Signup" v-if="disfiex == 1">
          <h2 class="overlaylongH2">注册</h2>
<!--          <input type="text" placeholder="用户名">-->
<!--          <input type="text" placeholder="用户密码">-->
<!--          <button class="inupbutton">注册</button>-->
          <el-form :model="dataDialogForm" :rules="rules" status-icon  ref="formSignUp">
            <el-form-item  prop="username">
              <el-input v-model="dataDialogForm.userName" placeholder="请输入账号"></el-input>
            </el-form-item>
            <el-form-item  prop="password" >
              <el-input type="password" v-model="dataDialogForm.password" show-password placeholder="请输入密码"></el-input>
            </el-form-item>
<!--            <h3>忘记密码请联系管理员</h3>-->
            <el-form-item>
              <el-button type="primary" size="medium" style="width: 324px ;" @click="submitFormDataToSign">注册</el-button>
            </el-form-item>
          </el-form>

        </div>

      </div>
      <div :class="overlaytitle">
        <div class="overlaytitle-Signin" v-if="disfiex == 0">
          <h2 class="overlaytitleH2">Hello,World!</h2>
          <p class="overlaytitleP">
            请输入您的账号密码以登录
          </p>
          <div class="buttongohs" @click="Signin">注册</div>
        </div>
        <div class="overlaytitle-Signup" v-if="disfiex == 1">
          <h2 class="overlaytitleH2">欢迎注册系统</h2>
          <p class="overlaytitleP">请输入注册相关信息</p>
          <div class="buttongohs" @click="Signup">登录</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  data() {
    return {
      overlaylong: 'overlaylong',
      overlaytitle: 'overlaytitle',
      disfiex: 0,
        form: {
          username: "",
          password: "",
        },
        rules: {
          username: [{ required: true, message: "请输入账号", trigger: "blur" }],
          password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        },
      dataDialogForm: {   //参数
        userName: "",
        password: "",
      },
    }
  },
  methods: {
    Signin() {
      this.overlaylong = "overlaylongleft"
      this.overlaytitle = "overlaytitleright"
      setTimeout(() => {
        this.disfiex = 1
      }, 200)
    },
    Signup() {
      this.overlaylong = "overlaylongright"
      this.overlaytitle = "overlaytitleleft"

      setTimeout(() => {
        this.disfiex = 0
      }, 200)

    },
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
                this.$router.push("/vr")
              } else {
                // 表示登录失败，显示错误提示
                this.$message.error(res.data.msg+"或者新员工未分配角色");
              }
            });
        } else {
          return false;
        }
      });
    },
    submitFormDataToSign() {
      // console.log(this.form);
      this.$refs['formSignUp'].validate((valid) => {
        //add调用---> saveOrUpdateRole 通过id(前端传的)判断是增加还是修改
        this.$http.post("/admin/signUp", this.dataDialogForm)
          .then((res) => {
            //this.dialogFormVisible =false;
            if (res.data.data = '成功') {
              this.$message({
                type: "success",
                message: "注册成功!",
              });
            }
            this.getDataList(); // 刷新数据
          });
      });
    }
  }

}

</script>
