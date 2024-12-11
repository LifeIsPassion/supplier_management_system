<template>
  <div>
    <!-- 商品库存列表 -->
    <el-card class=" box-card">
      <div>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataForm.select" placeholder="请输入商品名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click='getDataList'>查询</el-button>
            <el-button type="primary" @click="openDialog()">增加</el-button>
          </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="id" label="入库id" width="60">
          </el-table-column>
          <el-table-column prop="shopName" label="商品名称" width="80">
          </el-table-column>
          <el-table-column prop="shopNumber" label="库存" width="60">
          </el-table-column>
          <el-table-column prop="specs" label="计量单位" width="80">
          </el-table-column>
          <el-table-column prop="depository" label="存入仓库" width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="date" label="创建时间" width="170">
            <template slot-scope="scope">
              <span>{{scope.row.date ==null ? '': scope.row.date.replace("T"," ") }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="120" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope" width="120">
<!--              <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>-->
<!--              <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>-->
              <el-button size="mini" type="primary" @click="stockOut(scope.$index, scope.row)">出库</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[5, 7, 10]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>

      <!-- Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增商品库存' : '商品出库'" width="35%" :visible.sync="dialogFormVisible"
        @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="rules" ref="stockListForm">

          <el-form-item label="商品名称" label-width="120px" prop="shop">
            <template>
              <el-select style="width: 300px;" v-model="dataDialogForm.shop" placeholder="请选择">
                <el-option v-for="item in ShopIdAndName" :key="item.id" :label="'[' + item.id + '] '+ item.name"
                 :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="所属类" label-width="120px" prop="parentId">
             <template>
              <el-select style="width: 300px;" v-model="dataDialogForm.shopType" placeholder="请选择">
                <el-option v-for="item in shopTypeList" :key="item.id" :label="'[' + item.id + '] ' + item.shopType"
                  :value="item.shopType">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="数量" label-width="120px" prop="quantity">
            <el-input v-model="dataDialogForm.quantity" placeholder="数量" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="计量单位" label-width="120px" prop="speces">
            <el-input v-model="dataDialogForm.speces" placeholder="计量单位" style="width: 300px"></el-input>
          </el-form-item>

          <el-form-item label="存入仓库" label-width="120px" prop="depository">
            <template>
               <el-select style="width: 300px;" v-model="dataDialogForm.depository" placeholder="请选择">
                <el-option v-for="item in DepositoryAll" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="备注" label-width="120px" prop="remark">
            <el-input type="textarea" v-model="dataDialogForm.remark" style="width: 300px"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmitFormData('stockListForm')">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>
<script>
 import {regionData,CodeToText} from 'element-china-area-data' //地址数据
export default {
  name: "stockList",

  data() {
    return {

      options:[],
      options: regionData,  //级联地址内容
      addressData:{         	//存储地址数据
        areaCodes:"",
        addressName:""
      },


      DepositoryAll: [],
      //商品类型
      shopTypeList: [],
      ShopIdAndName:[],   //所有商品id和name
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
      dataDialogForm: {
        id: 0,
        shop: "",
        shopType: "",
        quantity: "",
        speces: "",
        depository: "",
        address: "",
        remark: "",
      },
      itemRowExitGoods:[],    //出库按钮行信息
      //验证
      rules: {
        //库存列表 如果有
        // roleName: [{ validator: checkRoleName, trigger: "blur" }],
        //  remark: [  { required: true, message: "请输入描述信息", trigger: "blur" } ],
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

    //地址 级联选择器
   handleChange (value) {
      //我们选择地址后，selectedOptions 会保存对应的区域码，例如北京的区域码为'110000'
      //我们要取出区域码对应的汉字，就需要用CodeToText(区域码)进行输出
      // console.log("regionData",regionData)
      // console.log("级联value",value);
      this.addressData.areaCodes = value;  //存储地址code

     //   console.log("this.addressData",this.addressData)
        var name = ''
        //name =CodeToText[code]
        this.areaCodes.map(item => name += CodeToText[item] + '/')  //由code集合遍历 寻找对应的地址名称 存为name
        this.dataDialogForm.address =name;
        this.addressData.addressName = name
        // console.log("地址:",this.addressData.name)
        // console.log("选择的全部信息:",this.addressData)
      //  this.addtions.name = ''
      },
    //出库
    stockOut (index,item) {
      this.itemRowExitGoods = item;
      this.$confirm('是否确定出库?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/purchase/stockOut" , this.itemRowExitGoods).then((res) =>{
          console.log("出库",res)
          if (res.data.data ==200 ) {
            this.$message({
              type: "success",
              message: "操作成功!",
            });
          }
          if (res.data.data ==1002 ) {
            this.$message({
              type: "error",
              message: res.data.message
            });
          }
        });
        this.getDataList();
      })
    },
    //编辑
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.shop = item.shop;
      this.dataDialogForm.shopType = item.shopType;
      this.dataDialogForm.shopName = item.shopName;
      this.dataDialogForm.quantity = item.quantity;
      this.dataDialogForm.speces = item.speces;
      this.dataDialogForm.depository = item.depository;
      this.addressData.addressName =item.address;
      this.dataDialogForm.remark = item.remark;

    },
    //编辑完成后-->确定提交  ruleFrom -->  formName
    handleSubmitFormData(formName) {
      this.update(formName);
    },
    update(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;

          this.$http.post("/stock/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
           //   this.addressData="",
              this.areaCodes="",
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: 0,
                shop: "",
                shopType: "",
                shopName:"",
                quantity: "",
                speces: "",
                depository: "",
                address: "",
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
            .post("/stock/delete?id=" + item.id)
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
    //添加角色--的打开窗口
    openDialog() {
      // 打开窗口
      this.dialogFormVisible = true;
      this.dataDialogForm.id = 0;
      this.dataDialogForm.shop = "";
      this.dataDialogForm.shopType = "";
      this.dataDialogForm.quantity = "";
      this.dataDialogForm.speces = "";
      this.dataDialogForm.depository = "";
      this.dataDialogForm.address = "";
      this.dataDialogForm.remark = "";
    },
    //关闭窗口
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        id: 0,
        shop: "",
        shopType: "",
        quantity: "",
        speces: "",
        depository: "",
        address: "",
        remark: "",
      };
    },

    //--------通过商品名称获取商品类型
    //     getShopTypeByName(){
    //    this.$http("/shop/getShopTypeByName").then((res)=>{
    //       console.log(res);
    // })
    // },

    //获取商品类型列表
    getShopTypeList() {
      this.$http.get("/shop/getShopTypeList").then((res) => {
        // console.log("商品类型", res)
        this.shopTypeList = res.data.data;
      })
    },

    //--------查询仓库方法
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
      this.getDepositoryAll();
      this.getShopTypeList();
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
      this.$http.get("/stock/list", params).then((res) => {
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
    this.getShopIdAndName();
  }
}
</script>
<style></style>


