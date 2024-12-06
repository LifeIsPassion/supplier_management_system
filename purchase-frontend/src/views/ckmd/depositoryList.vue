<template>
  <div>
    <!-- 仓库列表 -->
    <el-card class=" box-card">
      <div>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataForm.select" placeholder="请输入仓库名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="getDataList">查询</el-button>
            <el-button type="primary" @click="openDialog()">新增</el-button>
          </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="id" label="Id" width="50">
          </el-table-column>
          <el-table-column prop="name" label="名称" width="100">
          </el-table-column>
          <el-table-column prop="head" label="负责人" width="80">
          </el-table-column>
          <el-table-column prop="storePhone" label="电话" width="120">
          </el-table-column>
          <el-table-column prop="address" label="地址" width="80" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="stockTotal" label="总容量" width="80">
          </el-table-column>
          <!-- <el-table-column prop="surplus" label="剩余量" width="80">
          </el-table-column> -->
          <el-table-column prop="area" label="单位" width="80">
          </el-table-column>
          <el-table-column prop="buildDate" label="创建时间" width="180">
            <template slot-scope="scope">
              <span>{{scope.row.buildDate ==null ? '': scope.row.buildDate.replace("T"," ") }}</span>
            </template>
          </el-table-column>
          <!-- 0 正常 1 停用 -->
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.status == 0 ? '正常' : '禁用' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="120" show-overflow-tooltip>
          </el-table-column>

          <el-table-column label="操作" >
            <template slot-scope="scope" width="120">
              <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination @size-change="EventPageSize" @current-change="currentPage" :current-page="pageIndex"
          :page-sizes="[5, 7,10]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>

      <!-- Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增仓库' : '更新仓库'" width="35%" :visible.sync="dialogFormVisible"
        @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="Rules" ref="ruleForm">
          <el-form-item label="仓库名称" label-width="120px" prop="name">
            <el-input v-model="dataDialogForm.name" placeholder="仓库名称" style="width: 300px"></el-input>
          </el-form-item>
 
          <el-form-item label="负责人" label-width="120px" prop="head">
            <template>
              <!-- label是显示的东西  value是对应选中的值 -->
              <el-select style="width: 300px;" v-model="dataDialogForm.head" placeholder="请选择公司员工">
                <el-option v-for="item in adminAll" 
                :key="item.id" :label="'[' + item.id + '] '+ item.name"
                 :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="电话" label-width="120px" prop="PropStorePhone">
            <el-input v-model="dataDialogForm.storePhone" placeholder="电话" style="width: 300px"></el-input>
          </el-form-item>
          <!-- 级联 -->
          <el-form-item label="地址" label-width="120px" prop="address">
            <el-input v-model="addressData.addressName" placeholder="地址" disabled style="width: 200px"></el-input>
            <el-cascader size="large" :options="options" v-model="addressData.addressName" @change="ChangeAddress">
            </el-cascader>
          </el-form-item>

          <el-form-item label="仓库总容量" label-width="120px" prop="stockTotal">
            <el-input v-model="dataDialogForm.stockTotal" placeholder="仓库总容量(立方米)" style="width: 300px"></el-input>
          </el-form-item>
          <!-- <el-form-item label="仓库剩余容量" label-width="120px" prop="surplus">
            <el-input v-model="dataDialogForm.surplus" placeholder="仓库剩余容量" style="width: 300px"></el-input>
          </el-form-item> -->
          <!-- 统一设置为立方米 -->
          <!-- <el-form-item label="单位" label-width="120px" prop="area">
            <el-input v-model="dataDialogForm.area" placeholder="容量(立方米)" style="width: 300px"></el-input>
          </el-form-item> -->
          
          <el-form-item label="状态" label-width="120px" prop="status">
            <template> <!-- label是显示的东西  value是对应选中的值 -->
              <el-select style="width: 300px;" v-model="dataDialogForm.status" placeholder="请选择">
                <el-option v-for="item in statusTwo" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="描述信息" label-width="120px" prop="remark">
            <el-input type="textarea" v-model="dataDialogForm.remark" style="width: 300px"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmitFormData('ruleForm')">确 定</el-button>
        </div>
      </el-dialog>

    </el-card>

  </div>
</template>
<script>
import {regionData,CodeToText} from 'element-china-area-data' //地址数据
export default {
  name: "depository",

  data() {


    var checkDepositoryName = (rule, value, callback) => {
      if (this.dataDialogForm.id !== 0) {
        if (value === "") {
          callback(new Error("请输入仓库名称"));
        }
        // 说明是更新操作
        callback();
      } else if (value === "") {
        callback(new Error("请输入仓库名称"));
      } else {
        // console.log("获取value:", value);
        // 调用后端接口 检查 仓库名称是否存在
        this.$http.get("/depository/checkDepositoryName?depositoryName=" + value).then((res) => {
          // console.log("验证部门是否存在", res);
          if (res.data.data === "NO") {
            // 说明角色名不存在，可以使用
            callback();
          } else {
            callback(new Error("仓库名称重复"));
          }
        }).catch(console.error());
        //callback();
      }
    };
// 校验手机号位数
    var checkPhone =(rule,value,callback) =>{
      // console.log("校验开始-------")
      if (!Number.isInteger(value)) {
            callback(new Error('请输入数字值'));
          } else{
            if ( !Number.length(value) === 11) {
              callback(new Error('手机号必须是11位'));
            } else {
              callback();
            }
          }
    }

    return {
 

      //员工信息
      adminAll:[],
      // 弹窗需要校验的字段
      ruleForm:{
        name:'',
        PropStorePhone:'',
      },
      options: regionData,  //级联地址内容
      addressData:{         	//存储地址数据
        areaCodes:'',
        addressName:''
      },		
      //状态
      statusTwo: [
        { id: 0, name: '正常' }, 
        { id: 1, name: '禁用' }
      ],

      dataForm: {   select: "", },
      dataList: [],           //初始化数据列表   
      pageIndex: 1,           //初始页
      pageSize: 5,            //每页条数
      totalPage: 0,           //总条数
      dataListLoading: false,


      dialogFormVisible: false,               //编辑弹窗框
      dialogFormSubmitVisible: false,         //弹窗提交
      dataDialogForm: {                       //参数
        id: 0,
        name: "",
        head: "",
        storePhone: "",
        address: "",
        stockTotal: "",
        surplus: "",
        status: "",
        area: "",
        remark: "",
      },
      //----------------------- 用户校验---------前端提示
      Rules: {
        // name: [{ validate: checkDepositoryName, trigger: "blur" }],
        PropStorePhone : [{validate: checkPhone, trigger: "blur"  }]
        // storePhone: [{ required: true, message: "请输入11位数字", trigger: "blur" }],
      }
    }
  },
  methods: {
    //每页显示几条
    EventPageSize(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList()
    },
    //当前页
    currentPage(val) {
      this.pageIndex = val;
      this.getDataList()
    },

    // 打开窗口
    openDialog() {
      this.dialogFormVisible = true;
      this.dataDialogForm.id = 0;
      this.dataDialogForm.name = "";
      this.dataDialogForm.head = "",
        this.dataDialogForm.storePhone = "",
        this.dataDialogForm.address = "",
        this.dataDialogForm.stockTotal = "",
        this.dataDialogForm.surplus = "",
        this.dataDialogForm.area = "",
        this.dataDialogForm.remark = "";
    },
    //关闭窗口   取消或者确认都视为关闭窗口？
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        id: 0,
        name: "",
        head: "",
        storePhone: "",
        address: "",
        stockTotal: "",
        surplus: "",
        area: "",
        remark: "",
      };
    },

 //地址 级联选择器
 ChangeAddress (value) {
      //我们选择地址后，selectedOptions 会保存对应的区域码，例如北京的区域码为'110000'
      //我们要取出区域码对应的汉字，就需要用CodeToText(区域码)进行输出
      // console.log("regionData",regionData)
      // console.log("级联value",value);
      this.addressData.areaCodes = value;  //存储地址code
        var name = ''
        //name =CodeToText[code]
        this.addressData.areaCodes.map(item => name += CodeToText[item] + '/')  //由value集合遍历 寻找对应的地址名称 存为name
        this.dataDialogForm.address =name;  
        this.addressData.addressName = name;
        // console.log("地址:",this.addressData.name)
        // console.log("选择的全部信息:",this.addressData)
      },
    //编辑----------------------
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.name = item.name;
      this.dataDialogForm.head = item.head;
      this.dataDialogForm.storePhone = item.storePhone;
      this.dataDialogForm.address = item.address; //防止每次编辑后 未操作地址更新为空问题
      this.addressData.addressName =item.address;
      this.dataDialogForm.stockTotal = item.stockTotal;
      this.dataDialogForm.surplus = item.surplus;
      this.dataDialogForm.area = item.area;
      this.dataDialogForm.status = item.status;
      this.dataDialogForm.remark = item.remark;
      // console.log(this.dataDialogForm);
    },
    //编辑-->确定提交  ruleFrom -->  formName
    handleSubmitFormData(formName) {
      // console.log("弹窗地址问题--------",this.dataDialogForm)
      // console.log("可以替换的-------",this.dataDialogForm.address)
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
          this.$http.post("/depository/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
              // this.areaCodes="", 
              // this.addressName=''
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: 0,
                name: "",
                head: "",
                storePhone: "",
                address: "",
                stockTotal: "",
                surplus: "",
                area: "",
                // buildDate:"",
                remark: "",
              };
              // this.addressData.addressName =''
              this.dialogFormSubmitVisible = false;
              // 刷新数据
              this.getDataList();
            });
        } else {  return false;  }
      });
      //全局更新
        // this. getDataList();
       
    },


    // 删除用户信息------------------------
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
            .post("/depository/delete/?id=" + item.id)
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

    //查询员工方法
    getAdminAll() {
      this.$http.get("/admin/adminAll").then((res) => {
          console.log("全部员工", res);
        this.adminAll = res.data.data;
      })
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
      this.$http.get("/depository/list", params).then((res) => {
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
    this.getAdminAll()
  }
}
</script>
<style></style>
  
  
  