<template>
  <div>
    <!-- 客户资料 -->
    <el-card class="box-card">
      <div>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataForm.selectcustomer" placeholder="客户姓名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click='getDataList'>查询</el-button>
            <el-button type="primary" @click="openDialog()">增加</el-button>
          </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="id" label="Id" width="60">
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="80">
          </el-table-column>
          <el-table-column prop="phone" label="电话" width="120">
          </el-table-column>
          <el-table-column prop="address" label="地址" width="200"  show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" width="195" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="fax" label="传真" width="120"  show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="buildDate" label="创建时间" width="180">
            <template slot-scope="scope">
              <span>{{scope.row.buildDate ==null ? '': scope.row.buildDate.replace("T"," ") }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="branch" label="银行" width="150">
          </el-table-column>

          <el-table-column label="操作">
            <template slot-scope="scope" width="120">
              <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>

        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[5, 7, 10]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>


      <!-- Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增客户' : '更新客户'" width="30%" :visible.sync="dialogFormVisible"
        @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="rules" ref="customerForm">
          <el-form-item label="客户名称" label-width="120px" prop="name">
            <el-input v-model="dataDialogForm.name" placeholder="客户名称" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="客户电话" label-width="120px" prop="phone">
            <el-input v-model="dataDialogForm.phone" placeholder="客户电话" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="客户地址" label-width="120px" prop="address">
            <el-input v-model="dataDialogForm.address" placeholder="请输入客户/所在公司地址" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="客户邮箱" label-width="120px" prop="email">
            <el-input v-model="dataDialogForm.email" placeholder="客户邮箱" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="客户传真" label-width="120px" prop="fax">
            <el-input v-model="dataDialogForm.fax" placeholder="客户传真" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="银行" label-width="120px" prop="branch">
            <el-input v-model="dataDialogForm.branch" placeholder="银行名" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="银行卡号" label-width="120px" prop="branchNo">
            <el-input v-model="dataDialogForm.branchNo" placeholder="银行卡号" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="描述信息" label-width="120px" prop="remark">
            <el-input type="textarea" v-model="dataDialogForm.remark" style="width: 300px"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmitFormData('customerForm')">确 定</el-button>
        </div>
      </el-dialog>


    </el-card>
  </div>
</template>
<script>



export default {
  name: "customer",
  data() {

    //验证客户姓名是否
    return {
      //  tableData:[],
      dataForm: {
        selectcustomer: "",
      },
      dataList: [], //数据列表   
      pageIndex: 1,   //初始页
      pageSize: 5,        //每页条数
      totalPage: 0,         //总条数
      dataListLoading: false,

      //编辑弹窗框
      dialogFormVisible: false,
      dialogFormSubmitVisible: false,
      dataDialogForm: {
        id: "",
        name: "",
        phone: "",
        address: "",
        email: "",
        fax: "",
        branch: "",
        branchNo: "",
        remark: "",
      },
      //角色验证 validator: checkCustomerName
      rules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        phone: [{ required: true, message: "请输入电话", trigger: "blur" }],
        
      },
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

    //添加或者编辑客户 通过id来判断
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.name = item.name;
      this.dataDialogForm.phone = item.phone;
      this.dataDialogForm.address = item.address;
      this.dataDialogForm.email=item.email;
      this.dataDialogForm.fax = item.fax;
      this.dataDialogForm.branch = item.branch;
      this.dataDialogForm.branchNo = item.branchNo;
      this.dataDialogForm.remark = item.remark;
     
    },
    //编辑完成后-->确定提交  ruleFrom -->  formName
    handleSubmitFormData(formName) {
      this.updateRole(formName);
    },
    updateRole(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;

          this.$http.post("/customer/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: "",
                name: "",
                address: "",
                email: "",
                fax: "",
                branch: "",
                branchNo: "",
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
    //删除角色-----------------------------------
    handleDelete(index, item) {
      // 删除角色信息
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
            .post("/customer/delete?id=" + item.id)
            .then((res) => {
              // console.log(res)
              this.$message({
                type: "success",
                message: "删除成功!",
              });

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
    //添加角色--的打开窗口
    openDialog() {
      // 打开窗口
      this.dialogFormVisible = true;
      this.dataDialogForm.id=0;  //用于后端判断是否为更新 
      this.dataDialogForm.name = "";
      this.dataDialogForm.phone = "";
      this.dataDialogForm.address = "";
      this.dataDialogForm.email = "";
      this.dataDialogForm.fax = "";
      this.dataDialogForm.branch = "";
      this.dataDialogForm.branchNo = "";
      this.dataDialogForm.remark = "";
    },
    //关闭窗口
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        name: "",
        phone: "",
        address: "",
        email: "",
        fax: "",
        branch: "",
        branchNo: "",
        remark: "",
      };
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
          keyword: this.dataForm.selectcustomer,
        }

      };
      //后端请求 分页获取对象
      this.$http.get("/customer/listByName", params).then((res) => {
        // console.log("提交的参数", params);
        // console.log(res.data);
        //需要通过响应的结果配置
        this.dataList = res.data.data.list;
        this.totalPage = res.data.data.total;
        this.dataListLoading = false;

      });
    },

  },

  mounted() {
    this.getDataList()
  }
}
</script>
<style></style>
  
  
  