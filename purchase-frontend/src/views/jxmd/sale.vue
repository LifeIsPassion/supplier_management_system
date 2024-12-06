<template>
  <div>
    <el-card class=" box-card">
      <div>
        <el-form :inline="true" :model="dataform" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataform.select" placeholder="请输入采购单号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="info" icon="el-icon-search" @click="getDataList">查询</el-button>
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="openDialog()">新增</el-button>
          </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="id" label="ID" width="60">
          </el-table-column>
          <el-table-column prop="saleNumber" label="销售单号" width="100">
          </el-table-column>
          <el-table-column prop="saleUser" label="销售人" width="80">
          </el-table-column>
          <el-table-column prop="shop" label="销售商品" width="80">
          </el-table-column>
          <el-table-column prop="shopType" label="所属类" width="80">
          </el-table-column>
          <el-table-column prop="supplier" label="客户" width="80">
          </el-table-column>
          <el-table-column prop="specs" label="计量单位" width="100">
          </el-table-column>
          <el-table-column prop="num" label="销售数量" width="80">
          </el-table-column>
          <el-table-column prop="price" label="销售单价" width="100">
          </el-table-column>
          <el-table-column prop="totalPrice" label="销售总价" width="100">
          </el-table-column>
          <el-table-column prop="time" label="销售时间" width="180">
            <template slot-scope="scope">
              <span>{{ scope.row.time == null ? '' : scope.row.time.replace("T", " ") }}</span>
            </template>
          </el-table-column>
          <!-- 状态 0 完成 1未完成 3进行中 -->
          <el-table-column prop="status" label="状态" width="120" rules="rules">
            <template slot-scope="scope">
              <span>{{ scope.row.status == 0 ? '完成' : '进行中' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="120" show-overflow-tooltip>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template slot-scope="scope" label-width="auto" margin-left="0px">
              <el-button size="mini" type="primary" icon="el-icon-edit"
                @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" icon="el-icon-delete"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
              <el-button v-show="putStockID === 0" size="mini" type="primary" icon="el-icon-remove-outline"
                style="width: 75px; margin-left: 0% ; margin-top: 5px;"
                @click="putStock(scope.$index, scope.row)">出库</el-button>
              <el-button v-show="putStockID === 0" size="mini" type="primary" icon="el-icon-thumb"
                @click="inShop(scope.$index, scope.row)">回收</el-button>
            </template>
          </el-table-column>

        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[5, 7, 10]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>

      <!-- Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增销售单' : '更新销售单'" width="35%" :visible.sync="dialogFormVisible"
        @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="rules" ref="purchaseExitForm">

          <el-form-item label="销售人员" label-width="120px" prop="saleUser">
            <template>
              <!-- label是显示的东西  value是对应选中的值 -->
              <el-select style="width: 300px;" v-model="dataDialogForm.saleUser" placeholder="请选择公司员工">
                <el-option v-for="item in adminAll" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item v-show="dataDialogForm.id === 0" label="销售商品" label-width="120px" prop="shop">
            <template>
              <el-select style="width: 300px" v-model="dataDialogForm.shop" placeholder="请选择">
                <el-option v-for="item in shopAll" :key="item.id" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <!--  v-show="dataDialogForm.id === 0"  -->
          <el-form-item label="所属类" label-width="120px" prop="shopType">
            <template>
              <el-select style="width: 300px;" v-model="dataDialogForm.shopType" placeholder="请选择">
                <el-option v-for="item in shopTypeList" :key="item.id" :label="'[' + item.id + '] ' + item.shopType"
                  :value="item.shopType">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <!-- v-show="dataDialogForm.id === 0" -->
          <el-form-item label="客户" label-width="120px" prop="shop">
            <template>
              <el-select style="width: 300px" v-model="dataDialogForm.supplier" placeholder="请选择">
                <el-option v-for="item in customerAll" :key="item.id" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="销售数量" label-width="120px" prop="num">
            <el-input v-model="dataDialogForm.num" placeholder="销售数量" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="销售单价" label-width="120px" prop="price">
            <el-input v-model="dataDialogForm.price" style="width: 300px" placeholder="可保留两位小数"></el-input>
          </el-form-item>

          <el-form-item label="规格" label-width="120px" prop="specs">
            <template>
              <el-select style=" width: 300px;" v-model="dataDialogForm.specs" placeholder="请选择">
                <el-option v-for="item in specsList" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="状态" label-width="120px" prop="status">
            <template>
              <el-select style="width: 300px" v-model="dataDialogForm.status" placeholder="选填-----------默认:进行中">
                <el-option v-for="item in statusTwo" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                  :value="item.id">
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
          <el-button type="primary" @click="handleSubmitFormData('purchaseExitForm')">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 出库按钮弹窗 -->
      <el-dialog title="请选择仓库" :visible.sync="dialogPutSale">
        <el-form label-width="120px">
          <el-form-item label="出库仓库" label-width="120px">
            <template slot-scope="scope">
              <el-select v-model="PurchaseName" placeholder="请选择">
                <el-option v-for="item in DepositoryInfoAll" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogPutSale = false">取 消</el-button>
          <el-button type="primary" @click="submitSaleStock()">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 回收 -->
      <el-dialog title="销售退货原因" :visible.sync="dialogInGoods">
        <el-form label-width="120px">
          <el-form-item label="回收原因" label-width="120px">
            <el-input type="textarea" placeholder=" " v-model="remark" style="width: 300px;"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogInGoods = false">取 消</el-button>
          <el-button type="primary" @click="checkInGoods(remark)">确 定</el-button>
        </div>
      </el-dialog>

    </el-card>
  </div>
</template>
<script>
export default {
  name: "sale",
  data() {

    return {


      itemRowInGoods: [],    //回收 行信息
      dialogInGoods: false,
      remark: '',
      dialogPutSale: false,
      DepositoryInfoAll: [],
      PurchaseName: '',
      itemRowSaleInfo: [],   //出库操作的每一行的采购信息

      putStockID: 0,
      //公司员工
      adminAll: [],
      //状态
      statusTwo: [
        { id: 0, name: '完成' }, { id: 1, name: '进行中' }
      ],
      //  tableData:[],
      dataform: {
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
        saleNumber: '',
        saleUser: '',
        shop: '',
        shopType: '',
        supplier: '',
        num: '',
        quantity: '',
        price: '',
        specs: '',
        totalPrice: '',
        status: '',
        time: '',
        remark: '',
      },
      // 用户校验---------前端提示
      rules: {
        saleUser: [{ required: true, message: "请选择销售人员", trigger: "blur" }],
        shop: [{ required: true, message: "请选择销售商品", trigger: "blur" }],
        shopType: [{ required: true, message: "请选择商品类型", trigger: "blur" }]
        //   name: [{ validate: checkDepositoryName, trigger: "blur" }],
        // remark: [{ required: true, message: "请输入描述信息", trigger: "blur" }],
        //--------其他需要用的
      },
      shopAll: {},
      customerAll: {},
      //商品类型
      shopTypeList: [],

      //规格列表
      specsList: [
        { id: 0, name: '斤' }, { id: 1, name: '个' }, { id: 2, name: '件' },
        { id: 3, name: '箱' }
      ],
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
      this.dataDialogForm.saleNumber = '';
      this.dataDialogForm.saleUser = '',
        this.dataDialogForm.shop = '',
        this.dataDialogForm.shopType = '',
        this.dataDialogForm.supplier = '',
        this.dataDialogForm.num = '',
        this.dataDialogForm.quantity = '',
        this.dataDialogForm.price = '',
        this.dataDialogForm.specs = '',
        this.dataDialogForm.status = '',
        this.dataDialogForm.remark = '';


    },
    getShopNameAll() {
      //查询数据库中的所有商品名称---在新增操作时
      this.$http.get("/shop/getShopNameAll").then((res) => {
        // console.log("商品", res)
        this.shopAll = res.data.data;
      });
    },
    getCustomerAll() {
      //销售对象-------- 客户
      this.$http.get("/customer/getIDAndCustomerName").then((res) => {
        // console.log("客户", res);
        this.customerAll = res.data.data;
      });
    },
    //关闭窗口
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        id: 0,
        saleNumber: '',
        saleUser: '',
        shop: '',
        shopType: '',
        supplier: '',
        num: '',
        quantity: '',
        price: '',
        totalPrice: '',
        specs: '',
        status: '',
        time: '',
        remark: '',
      };
    },


    //编辑----------------------
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.number = item.number;
      this.dataDialogForm.saleNumber = item.saleNumber;
      this.dataDialogForm.saleUser = item.saleUser;
      this.dataDialogForm.supplier = item.supplier;
      this.dataDialogForm.shop = item.shop;
      this.dataDialogForm.shopType = item.shopType;
      this.dataDialogForm.num = item.num;
      this.dataDialogForm.quantity = item.quantity;
      this.dataDialogForm.price = item.price;
      this.dataDialogForm.specs = item.specs;
      this.dataDialogForm.totalPrice = item.totalPrice;
      this.dataDialogForm.status = item.status;
      this.dataDialogForm.time = item.time;
      this.dataDialogForm.remark = item.remark;

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
          this.$http.post("/sale/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: 0,
                saleNumber: '',
                saleUser: '',
                shop: '',
                supplier: '',
                num: '',
                quantity: '',
                price: '',
                specs: '',
                totalPrice: '',
                status: '',
                time: '',
                remark: '',
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
            .post("/sale/delete/?id=" + item.id)
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

    //获取商品类型列表
    getShopTypeList() {
      this.$http.get("/shop/getShopTypeList").then((res) => {
        // console.log("商品类型", res)
        this.shopTypeList = res.data.data;
      })
    },

    //查询员工方法
    getAdminAll() {
      this.$http.get("/admin/adminAll").then((res) => {
        console.log("全部员工", res);
        this.adminAll = res.data.data;
      })
    },
    //----+++++++查询仓库方法  异步
    async getDepositoryAll() {
      this.$http.get("/depository/listAll").then((res) => {
        this.DepositoryInfoAll = res.data.data;
      })
    },

    //出库操作
    putStock(index, item) {
      this.dialogPutSale = true;
      this.getDepositoryAll();
      this.itemRowSaleInfo = item;
    },
    //出库确认按钮
    submitSaleStock() {
      const params = {  //参数转
        params: {
          saleList: this.itemRowSaleInfo
        }
      }
      if (this.PurchaseName === '') {
        this.$message('请先选择入库的仓库哦！');
        return;
      }
      this.$http.post("/sale/outSale?depositoryName=" + this.PurchaseName, this.itemRowSaleInfo).then((res) => {
        //出库
        // console.log("出库", res);
        if (res.data.code  != 200) {
          this.$message({
            type: 'error',
            message: res.data.message
          })
        } else {
          this.$message({
            type: 'success',
            message: res.data.message
          })
        }
      })
      this.PurchaseName = ''
      this.dialogPutSale = false;
    },

    //-----------------回收
    inShop(index, item) {
      this.dialogInGoods = true;
      this.itemRowInGoods = item;
    },
    //-----------回收确认
    checkInGoods(remark) {
      this.$http.post("/sale/checkInGoods?remark=" + remark, this.itemRowInGoods).then((res) => {
        //回收确认
        console.log("回收",res)
        if (res.data.code  != 200) {
          this.$message({
            type: 'error',
            message: res.data.message
          })
        }  else {
          this.$message({
            type: 'success',
            message: res.data.message
          })
        }
        
      })
      this.dialogInGoods = false;
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
          keyword: this.dataform.select,
        }

      };
      //后端请求 分页获取对象
      this.$http.get("/sale/list", params).then((res) => {
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
    this.getDataList()
    this.getShopNameAll()
    this.getCustomerAll()
    this.getAdminAll()
    this.getShopTypeList();
  }
}
</script>
<style></style>
  
  