<template>
  <div>
    <!-- 仓库列表 -->
    <el-card class=" box-card">
      <div>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataForm.select" placeholder="请输入入库编号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="getDataList">查询</el-button>
            <el-button type="primary" @click="openDialog()">新增</el-button>
          </el-form-item>
        </el-form>

 
        <el-table ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="56">
          </el-table-column>
          <el-table-column prop="id" label="Id" width="60">
          </el-table-column>
          <el-table-column prop="inId" label="入库号" width="100">
          </el-table-column>
          <el-table-column prop="depository" label="入库仓库" width="100">
          </el-table-column>
          <el-table-column prop="shopName" label="入库商品" width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="shopNumber" label="入库数量" width="80">
          </el-table-column>
          <el-table-column prop="specs" label="规格" width="60">
          </el-table-column>
          <el-table-column prop="shopPrice" label="入库单价" width="80">
          </el-table-column>
          <el-table-column prop="priceTotal" label="总价" width="60">
          </el-table-column>
          <el-table-column prop="inUser" label="入库人" width="80">
          </el-table-column>
          <el-table-column prop="status" label="是否入库" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.status == 0 ? "已入库" : "未入库" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="isInspection" label="是否质检" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.isInspection == 0 ? "已质检" : "未质检" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="date" label="入库时间" width="170">
          </el-table-column>
          <el-table-column prop="date" label="创建时间" width="170">
            <template slot-scope="scope">
              <span>{{scope.row.date ==null ? '': scope.row.date.replace("T"," ") }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="80" show-overflow-tooltip>
          </el-table-column>

          <el-table-column  label="操作" fixed="right"  width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
              <el-button size="mini" type="primary" style="margin-left: 0% ; margin-top: 5px;"
                @click="check(scope.$index, scope.row)">质检</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[5, 7,10]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>

      
      <!-- Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增入库' : '更新入库'" width="35%" :visible.sync="dialogFormVisible"
        @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="rules" ref="depositoryForm">
         
          <el-form-item label="入库仓库" label-width="120px" prop="depository">
            <template>
              <!-- label是显示的东西  value是对应选中的值 -->
              <el-select style="width: 300px;" v-model="dataDialogForm.depository" placeholder="请选择">
                <el-option v-for="item in DepositoryAll" 
                :key="item.id" :label="'[' + item.id + '] '+ item.name"
                 :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="入库商品" label-width="120px" prop="shopName">
            <template>
              <el-select style="width: 300px;" v-model="dataDialogForm.shopName" placeholder="请选择">
                <el-option v-for="item in ShopIdAndName" :key="item.id" :label="'[' + item.id + '] '+ item.name"
                 :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="入库数量" label-width="120px" prop="shopNumber">
            <el-input v-model="dataDialogForm.shopNumber" placeholder="入库数量" style="width: 300px"></el-input>
          </el-form-item>
 
          <el-form-item label="规格" label-width="120px" prop="specs">
            <template> <!-- label是显示的东西  value是对应选中的值 -->
              <el-select style="width: 300px;" v-model="dataDialogForm.specs" placeholder="请选择">
                <el-option v-for="item in specsList" 
                :key="item.id" :label="'['+item.id+'] '+item.name" :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>


          <el-form-item label="入库单价" label-width="120px" prop="shopPrice">
            <el-input v-model="dataDialogForm.shopPrice" placeholder="入库单价" style="width: 300px"></el-input>
          </el-form-item>
 
          <el-form-item label="描述信息" label-width="120px" prop="remark">
            <el-input type="textarea" v-model="dataDialogForm.remark" style="width: 300px"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmitFormData('depositoryForm')">确 定</el-button>
        </div>
      </el-dialog>

    </el-card>
  </div>
</template>
<script>



export default {
  name: "depositoryIn",

  data() {
      //校验入库商品
    var checkDepositoryInName = (rule, value, callback) => {

      if (this.dataDialogForm.id !== 0) {
        if (value === "") {
          callback(new Error("请输入商品"));
        }
        // 说明是更新操作
        callback();
      } else if (value === "") {
        callback(new Error("请输入商品名称"));
      } else {
        // console.log("获取value:", value);
        // 调用后端接口 检查 编号名称是否存在
        this.$http.get("/depositoryIn/checkDepositoryInId?DepositoryInId=" + value).then((res) => {
          // console.log("验证部门是否存在", res);
          if (res.data.data === "NO") {
            // 说明角色名不存在，可以使用
            callback();
          } else {
            callback(new Error("商品重复"));
          }
        }).catch(console.error());
        //callback();
      }
    };

    return {


      DepositoryAll: [],  //所有仓库信息
      ShopIdAndName:[],   //所有商品id和name
      //状态 是否入库
      statusTwo: [
        { id: 0, name: '已入库' }, { id: 1, name: '未入库' }
      ],

      //规格列表
      specsList:[
      { id: 0, name:'斤' }, { id: 1, name: '个' },{id:2,name:'件' },
      {id:3, name:'箱'}
      ] ,  


      dataForm: {
        select: "",
      },
      dataList: [], //数据列表   
      pageIndex: 1,   //初始页
      pageSize: 5,        //每页条数
      totalPage: 0,         //总条数
      dataListLoading: false,


      //编辑弹窗框
      dialogFormVisible: false,
      dialogFormSubmitVisible: false,
      dataDialogForm: {   //参数
        id: 0,
        inId: "",
        depository: "",
        shopName: "",
        shopNumber: "",
        specs: "",
        shopPrice: "",
        status: "",
        inUser: "", //入库人
        remark: "",
      },
      // 用户校验---------前端提示
      rules: {
        name: [{ validate: checkDepositoryInName, trigger: "blur" }],
        // remark: [{ required: true, message: "请输入描述信息", trigger: "blur" }],
        //--------其他需要用的
      }
    }
  },
  methods: {
    //每页显示几条
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList()
    },
    //当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList()
    },

    // 新增窗口
    openDialog() {
      this.dialogFormVisible = true;
      this.dataDialogForm.id = 0;
      this.dataDialogForm.inId = "";
      this.dataDialogForm.depository = "",
        this.dataDialogForm.shopName = "",
        this.dataDialogForm.shopNumber = "",
        this.dataDialogForm.specs = "",
        this.dataDialogForm.shopPrice = "",
        this.dataDialogForm.status = "",
        this.dataDialogForm.remark = "";

    },
    //关闭窗口
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        id: 0,
        inId: "",
        depository: "",
        shopName: "",
        shopNumber: "",
        specs: "",
        shopPrice: "",
        status: "",
        remark: "",
      };
    },


    //编辑----------------------
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.inId = item.inId;
      this.dataDialogForm.depository = item.depository;
      this.dataDialogForm.shopName = item.shopName;
      this.dataDialogForm.shopNumber = item.shopNumber;
      this.dataDialogForm.specs = item.specs;
      this.dataDialogForm.shopPrice = item.shopPrice;
      this.dataDialogForm.status = item.status;
      this.dataDialogForm.remark = item.remark;
      //ssion中获取用户名
      this.dataDialogForm.inUser = sessionStorage.getItem('username');
      // console.log(this.dataDialogForm);
    },
    //编辑-->确定提交  ruleFrom -->  formName
    handleSubmitFormData(formName) {
      this.update(formName);
    },

    //更新 
    update(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;

          //add调用---> saveOrUpdateRole 通过id(前端传的)判断是增加还是修改
          this.$http.post("/depositoryIn/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: 0,
                inId: "",
                depository: "",
                shopName: "",
                shopNumber: "",
                specs: "",
                shopPrice: "",
                status: "",
                remark: "",
              };
              this.dialogFormSubmitVisible = false;
              // 刷新数据
              this.getDataList();
            });
        } else {
          // console.log('error submit!!');
          return false;
        }
      });
    },


    // 删除信息------------------------
    handleDelete(index, item) {

      this.$confirm("此操作将永久该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;
          this.$http
            .post("/depositoryIn/delete/?id=" + item.id)
            .then((res) => {
              // console.log(res)
              if (res.data.data === '0') {
                // 表示数据不能被删除
                this.$message({
                  type: "warning",
                  message: "该条记录不允许删除!",
                });
              } else {
                this.$message({
                  type: "success",
                  message: "删除成功!",
                });
              }
              this.dialogFormSubmitVisible = false;
              // 刷新数据
              this.getDataList();
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //

    //查询仓库方法
    getDepositoryAll() {
      this.$http.get("/depository/listAll").then((res) => {
        // console.log("全部仓库", res);
        this.DepositoryAll = res.data.data;
      })
    },

    //查询 全部商品 id:name   key:value
      getShopIdAndName(){
        this.$http.get("/shop/getShopNameAll").then((res)=>{
            // console.log("商品信息",res)
            this.ShopIdAndName =res.data.data;
        })
      },
    //-------------------------------------
    //获取初始化数据
    getDataList() {
      if (this.dataListLoading) {
        return;
      }

      //请求参数封装
      const params = {
        params: {
          pageSize: this.pageSize,
          pageNum: this.pageIndex,
          keyword: this.dataForm.select,
        }

      };
      //后端请求 分页获取对象
      this.$http.get("/depositoryIn/list", params).then((res) => {
        // console.log("提交的参数", params);
        //需要通过响应的结果配置
        this.dataList = res.data.data.list;
        this.totalPage = res.data.data.total;
        this.dataListLoading = false;

      });
    },

    //-----------质检 check
    check(index,item){
      this.$confirm('确认质检后将正式存入仓库, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post("/depositoryIn/checkById?id="+item.id).then((res=>{
              // console.log(res)
              if(res.data.code ===200){
                this.$message({
                type: 'success',
                message: '商品质检完成!'
          });
          this.getDataList();    //完成就更新操作
         }else{
            this.$message({
                type: 'error',
                message: '操作错误,请联系管理员!'
          });
          }
          }))
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          });          
        });
        this.getDataList();
    }

  },

  mounted() {
      this.getDataList()
      this.getDepositoryAll();   //怎么异步
      this.getShopIdAndName();
  }
}
</script>
<style></style>
  
  
  