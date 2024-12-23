<template>
  <div>
    <el-card class=" box-card">
      <div>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataForm.select" placeholder="请输入采购单号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="info" icon="el-icon-search"  @click="getDataList">查询</el-button>
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="openDialog()">新增</el-button>
            <!-- <el-button type="primary">导出excel</el-button> -->
          </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="id" label="Id" width="60">
          </el-table-column>
          <el-table-column prop="number" label="采购编号" width="100">
          </el-table-column>
          <el-table-column prop="exitNumber" label="退货编号" width="160">
          </el-table-column>
          <el-table-column prop="num" label="退购数量" width="80">
          </el-table-column>
          <el-table-column prop="price" label="退购单价" width="80">
          </el-table-column>
          <el-table-column prop="totalPrice" label="退购总价" width="100">
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.status == 0 ? '完成' : '进行中' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="退货时间" width="280">
            <template slot-scope="scope">
              <span>{{scope.row.time ==null ? '': scope.row.time.replace("T"," ") }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="退购原因" width="280" show-overflow-tooltip>
          </el-table-column>


        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[5,7, 10]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>

      <!-- Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增采购退货单' : '更新采购退货单'" width="35%" :visible.sync="dialogFormVisible"
        @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="rules" ref="purchaseExitForm">

          <el-form-item label="采购编号" label-width="120px" prop="number">
            <el-autocomplete style="width: 300px;"  popper-class="my-autocomplete" v-model="dataDialogForm.number" :fetch-suggestions="querySearch"
            placeholder="请输入内容" @select="handleSelect">
            <i class="el-icon-edit el-input__icon" slot="suffix" @click="handleIconClick">
            </i>
            <template slot-scope="{ item }">
              <div class="name">{{ item.value }}</div>
              <span class="addr">{{ item.address }}</span>
            </template>
          </el-autocomplete>
          </el-form-item>

          <el-form-item label="退购数量" label-width="120px" prop="num">
            <el-input v-model="dataDialogForm.num" placeholder="退购数量" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="退购单价" label-width="120px" prop="price">
            <el-input v-model="dataDialogForm.price" placeholder="退购单价" style="width: 300px"></el-input>
          </el-form-item>

          <el-form-item label="状态" label-width="120px" prop="status">
            <template>
              <el-select style="width: 300px;" v-model="dataDialogForm.status" placeholder="请选择">
                <el-option v-for="item in statusTwo" :key="item.id" :label="'['+item.id+']'+item.name" :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="退购原因" label-width="120px" prop="reason">
            <el-input type="textarea" v-model="dataDialogForm.reason" style="width: 300px"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmitFormData('purchaseExitForm')">确 定</el-button>
        </div>
      </el-dialog>


    </el-card>
  </div>
</template>
<script>
export default {

  name: "purchaseExit",
  data() {
    return {

      //状态
      statusTwo: [{ id: 0, name: '完成' },
      { id: 1, name: '进行中' }],

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
      dataDialogForm: {   //参数 这里编写的是table的展示数据
        id: 0,
        number: "",
        exitNumber: "",
        num: "",
        price: "",
        totalPrice: "",
        status: "",
        time: "",
        reason: "",
      },
      // 用户校验---------前端提示
      rules: {
        //   name: [{ validate: checkDepositoryName, trigger: "blur" }],
        // remark: [{ required: true, message: "请输入描述信息", trigger: "blur" }],
        //--------其他需要用的
      },
    //dialog的供应商自定义搜索
    restaurants: [],

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


    // 打开窗口
    openDialog() {
      this.dialogFormVisible = true;
      this.dataDialogForm.id = 0;
      this.dataDialogForm.purchaseUser = "";
      this.dataDialogForm.shop = "",
        this.dataDialogForm.supplier = "",
        this.dataDialogForm.quantity = "",
        this.dataDialogForm.price = "",
        this.dataDialogForm.status = "",
        this.dataDialogForm.remark = "";
    },
    //关闭窗口
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        id: 0,
        number: "",
        exitNumber: "",
        num: "",
        price: "",
        totalPrice: "",
        status: "",
        time: "",
        reason: "",
      };
    },


    //编辑----------------------
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.number = item.number;
      this.dataDialogForm.exitNumber = item.exitNumber;
      this.dataDialogForm.num = item.num;
      this.dataDialogForm.price = item.price;
      this.dataDialogForm.totalPrice = item.totalPrice;
      this.dataDialogForm.status = item.status;
      this.dataDialogForm.time = item.time;
      this.dataDialogForm.reason = item.reason;
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
          this.$http.post("/purchaseExit/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: 0,
                number: "",
                exitNumber: "",
                num: "",
                price: "",
                totalPrice: "",
                status: "",
                time: "",
                reason: "",
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
            .post("/purchaseExit/delete/?id=" + item.id)
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

  //-------------------------dialog搜索
  querySearch(queryString, cb) {
      // console.log("执行了",this.restaurants)
         var restaurants = this.restaurants;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },

      loadAll() {
         this.$http.get("/purchase/getNumberAndSupplierName").then((res)=>{
                // console.log("name-address",res)
                 this.restaurants =  res.data.data;
             })
      },
      handleSelect(item) {
        // console.log("item",item);
      },
      handleIconClick(ev) {
        // console.log("ev",ev);
      },



    //-------------------------------------
    //获取初始化数据
    getDataList() {
      if (this.dataListLoading) {
        return;
      }
      // this.dataListLoading=true;
      //请求参数封装
      const params = {
        params: {
          pageSize: this.pageSize,
          pageNum: this.pageIndex,
          keyword: this.dataForm.select,
        }

      };
      //后端请求 分页获取对象
      this.$http.get("/purchaseExit/list", params).then((res) => {
        // console.log("提交的参数", params);
        // console.log(res);
        //需要通过响应的结果配置
        this.dataList = res.data.data.list;
        this.totalPage = res.data.data.total;
        this.dataListLoading = false;

      });
    },

  },

  mounted() {
    this.getDataList(),
    this.loadAll();
  }
}
</script>
<style lang="less" scoped>
/* 供应商 可搜索 */
.my-autocomplete {
  li {
    line-height: normal;
    padding: 7px;

    .name {
      text-overflow: ellipsis;
      overflow: hidden;
    }
    .addr {
      font-size: 12px;
      color: #b4b4b4;
    }

    .highlighted .addr {
      color: #ddd;
    }
  }
}
</style>


