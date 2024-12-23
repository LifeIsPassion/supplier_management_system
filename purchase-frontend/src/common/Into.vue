<style>
.center {
  /*width: 1920px;*/
  /*height: 1080px;*/
  width: 84vw; /* 宽度设置为视口宽度的100% */
  height: 90vh; /* 高度设置为视口高度的100% */
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
    <h1 style="color: #FFFFFF;position: absolute; top: 200px; left: 600px;font-size: 40px;">欢迎进入供应商自助管理系统</h1>
    <h1 style="color: #FFFFFF;position: absolute; top: 300px; left: 620px;font-size: 30px;">当前时间：{{date}}</h1>
    <h1 style="color: #FFFFFF;position: absolute; top: 400px; left: 620px;font-size: 30px;">初次登录系统请先完善个人信息</h1>
    <!--    <div id="app"; style="color: #FFFFFF;position: absolute; top: 200px; left: 600px;font-size: 100px;">-->
<!--      当前时间：{{date}}-->
<!--    </div>-->
  </div>
</template>

<script>
export default {
  data() {
    return {
      overlaylong: 'overlaylong',
      overlaytitle: 'overlaytitle',
      date: new Date(),
      disfiex: 0,
      form: {
        username: "",
        password: "",
      },
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },

    }
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
    }
  },
  mounted() {
    let _this = this; // 声明一个变量指向Vue实例this，保证作用域一致
    this.timer = setInterval(() => {
      _this.date = this.formatDate(new Date()); // 修改数据date
    }, 1000)
  },
  methods: {
    formatDate(date) {
      const year = date.getFullYear(); // 年份
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份，+1是因为getMonth()返回的月份是从0开始的
      const day = date.getDate().toString().padStart(2, '0'); // 日
      const hours = date.getHours().toString().padStart(2, '0'); // 小时
      const minutes = date.getMinutes().toString().padStart(2, '0'); // 分钟
      const seconds = date.getSeconds().toString().padStart(2, '0'); // 秒

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`; // 返回格式化后的日期字符串
    },
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
    }
  }

}

</script>
