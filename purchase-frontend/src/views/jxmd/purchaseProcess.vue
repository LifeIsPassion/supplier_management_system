<template>
  <div>
    <!-- 采购 -->
    <el-card class="box-card">
      <div>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="dataForm.select" placeholder="请输入采购人姓名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="info" icon="el-icon-search" @click="getDataList">查询</el-button>
<!--            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="openDialog()">新增</el-button>-->
          </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="Datalist" tooltip-effect="dark" style="width: 100%">
          <el-table-column type="selection" width="42">
          </el-table-column>
          <el-table-column prop="id" label="Id" width="40">
          </el-table-column>
          <el-table-column prop="number" label="采购编号" width="100">
          </el-table-column>
          <el-table-column prop="purchaseUser" label="采购人" width="80">
          </el-table-column>
          <el-table-column prop="shop" label="采购商品" width="100">
          </el-table-column>
          <el-table-column prop="shopType" label="所属类" width="80">
          </el-table-column>
          <el-table-column prop="supplier" label="采购供应商" width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="specs" label="计量单位" width="100">
          </el-table-column>
          <el-table-column prop="quantity" label="采购数量" width="80">
          </el-table-column>
          <el-table-column prop="shopSpace" label="商品所占空间" width="80">
          </el-table-column>
          <el-table-column prop="price" label="单价" width="80">
          </el-table-column>
          <el-table-column prop="totalPrice" label="总价" width="80">
          </el-table-column>
          <el-table-column prop="time" label="采购时间" width="180">
            <template slot-scope="scope">
              <span>{{ scope.row.time == null ? '' : scope.row.time.replace("T", " ") }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="sendStatus" label="发货状态" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.sendStatus == 0 ? '已发货' : '未发货' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="采购状态" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.status == 0 ? '已完成采购' : '进行中' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="isIn" label="是否入库" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.isIn == 0  ? '已入库' : '未入库' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="120" show-overflow-tooltip>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template slot-scope="scope" label-width="auto" margin-left="0px">
              <el-button size="mini" type="primary" icon="el-icon-edit"
                         @click="handleOk(scope.$index, scope.row)">完成采购</el-button>
              <el-button v-show="putStockID === 0" size="mini" type="primary" icon="el-icon-thumb"
                         style="width: 75px; margin-left: 0% ; margin-top: 5px;"
                         @click="inStock(scope.$index, scope.row)">入库</el-button>
              <el-button v-show="putStockID === 0" size="mini" type="primary" icon="el-icon-remove-outline"
                         @click="exitShop(scope.$index, scope.row)">退货</el-button>
            </template>
          </el-table-column>

        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                       :page-sizes="[5, 7, 10]" :page-size="pageSize" :total="totalPage"
                       layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
        </el-pagination>
      </div>

      <!-- 新增/编辑    Form -->
      <el-dialog :title="dataDialogForm.id === 0 ? '新增采购单' : '更新采购单'" width="35%" :visible.sync="dialogFormVisible"
                 @close="closeDialog()">
        <el-form :model="dataDialogForm" :rules="rules" ref="depositoryForm">
          <el-form-item label="采购员工" label-width="120px" prop="purchaseUser">
            <template>
              <!-- label是显示的东西  value是对应选中的值 -->
              <el-select style="width: 300px;" v-model="dataDialogForm.purchaseUser" placeholder="请选择公司员工">
                <el-option v-for="item in adminAll" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                           :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="采购商品" label-width="120px" prop="shop">
            <template>
              <el-select style=" width: 300px;" v-model="dataDialogForm.shop" placeholder="请选择">
                <el-option v-for="item in shopAll" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                           :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="所属类" label-width="120px" prop="parentId">
            <template>
              <el-select style=" width: 300px;" v-model="dataDialogForm.shopType" placeholder="请选择">
                <el-option v-for="item in shopTypeList" :key="item.id" :label="'[' + item.id + '] ' + item.shopType"
                           :value="item.shopType">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <el-form-item label="采购供应商" label-width="120px" prop="supplier">
            <el-autocomplete style=" width: 300px;" popper-class="my-autocomplete" v-model="dataDialogForm.supplier"
                             :fetch-suggestions="querySearch" placeholder="请输入内容" @select="handleSelect">
              <i class="el-icon-edit el-input__icon" slot="suffix" @click="handleIconClick">
              </i>
              <template slot-scope="{ item }">
                <div class="name">{{ item.value }}</div>
                <span class="addr">{{ item.address }}</span>
              </template>
            </el-autocomplete>
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

          <el-form-item label="采购数量" label-width="120px" prop="quantity">
            <el-input v-model="dataDialogForm.quantity" placeholder="采购数量" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="采购单价" label-width="120px" prop="price">
            <el-input v-model="dataDialogForm.price" placeholder="采购单价" style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="状态" label-width="120px" prop="status">
            <template>
              <el-select style=" width: 300px;" v-model="dataDialogForm.status" placeholder="请选择">
                <el-option v-for="item in statusTwo" :key="item.id" :label="'[' + item.id + ']' + item.name"
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
          <el-button type="primary" @click="handleSubmitFormData('depositoryForm')">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 入库按钮弹窗 -->
      <el-dialog title="请选择仓库" :visible.sync="dialogPutStock">
        <el-form label-width="120px">
          <el-form-item label="入库仓库" label-width="120px">
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
          <el-button @click="dialogPutStock = false">取 消</el-button>
          <el-button type="primary" @click="submitStock()">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 退货操作按钮 -->
      <el-dialog title="退货原因" :visible.sync="dialogExitGoods">
        <el-form label-width="120px">
          <el-form-item label="退货原因" label-width="120px">
            <el-input type="textarea" placeholder=" " v-model="remark"  style="width: 300px;" ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogExitGoods = false">取 消</el-button>
          <el-button type="primary" @click="checkExitGoods(remark)">确 定</el-button>
        </div>
      </el-dialog>

    </el-card>
  </div>
</template>
<script>
//变量 “” 与 ''区别    写null?
//   一般情况下两者是通用的.但双引号内部变量会解析,单引号则不解析.
//一般情况下两者是通用的.但双引号内部变量会解析,单引号则不解析.
export default {
  name: 'purchase',

  data() {

    var checkDepositoryArea = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请选择仓库"));
      }
      console.log("选中的仓库", value)
    }
    return {
      remark:'',
      putStockID: 0,
      adminAll: [],
      //--------------------入库按钮查询
      DepositoryInfoAll: [],
      PurchaseName: '',
      shopAll: {   id: '',  name: '', },
      //状态
      statusTwo: [{ id: 0, name: '完成' },
        { id: 1, name: '进行中' }],

      dataForm: {
        select: "",
      },
      //规格列表
      specsList: [
        { id: 0, name: '斤' }, { id: 1, name: '个' }, { id: 2, name: '件' },
        { id: 3, name: '箱' }
      ],

      //入库选择仓库规则
      Datalist: [], //数据列表
      pageIndex: 1,   //初始页
      pageSize: 5,        //每页条数
      totalPage: 0,         //总条数
      dialogExitGoods: false, //退货
      dataListLoading: false,
      dialogPutStock: false,  //入库按钮
      //编辑弹窗框
      dialogFormVisible: false,
      dialogFormSubmitVisible: false,
      dataDialogForm: {   //参数
        id: 0,
        number: '',
        purchaseUser: '',
        shop: '',
        shopType: '',
        supplier: '',
        quantity: '',
        price: '',
        totalPrice: '',
        specs: '',
        time: '',
        status: '',
        remark: '',
        isIn: '',
        shopSpace: '',
      },
      itemRowPurchaseInfo: [],   //入库操作的每一行的采购信息
      itemRowExitGoods:[],    //退货按钮行信息
      //dialog的供应商自定义搜索
      restaurants: [],
      shopTypeList: [],
      // 用户校验---------前端提示
      rules: {  },

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
      this.dataDialogForm.purchaseUser = '',
        this.dataDialogForm.shop = '',
        this.dataDialogForm.shopType = '',
        this.dataDialogForm.supplier = '',
        this.dataDialogForm.quantity = '',
        this.dataDialogForm.specs = '',
        this.dataDialogForm.price = '',
        this.dataDialogForm.status = '',
        this.dataDialogForm.remark = '';
    },
    //关闭窗口
    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        id: 0,
        //  number: "",
        purchaseUser: '',
        shop: '',
        shopType: '',
        supplier: '',
        quantity: '',
        price: '',
        status: '',
        specs: '',
        remark: '',
      };
    },


    //编辑----------------------
    handleEdit(index, item) {
      this.dialogFormVisible = true;   // 打开更新的窗口
      // 绑定需要更新的数据  数据是table里的prop
      this.dataDialogForm.id = item.id;
      this.dataDialogForm.purchaseUser = item.purchaseUser;
      this.dataDialogForm.shop = item.shop;
      this.dataDialogForm.shopType = item.shopType;
      this.dataDialogForm.supplier = item.supplier;
      this.dataDialogForm.quantity = item.quantity;
      this.dataDialogForm.price = item.price;
      this.dataDialogForm.specs = item.specs;
      this.dataDialogForm.status = item.status;
      this.dataDialogForm.remark = item.remark;
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
          this.$http.post("/purchase/add", this.dataDialogForm)
            .then((res) => {
              // console.log("添加/更新", res);
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                id: 0,
                purchaseUser: '',
                shop: '',
                shopType: '',
                supplier: '',
                quantity: '',
                price: '',
                status: '',
                specs: '',
                remark: '',
              };
              this.dialogFormSubmitVisible = false;
              // 刷新数据
              this.getDataList();
            });
        } else {
          return false;
        }
      });
    },

    dataProcess() {
      this.dataDialogForm.purchaseUser = null;
      this.dataDialogForm.shop = null;
      this.dataDialogForm.number = null;
      this.dataDialogForm.shopType = null;
      this.dataDialogForm.supplier = null;
      this.dataDialogForm.quantity = null;
      this.dataDialogForm.price = null;
      this.dataDialogForm.specs = null;
      this.dataDialogForm.remark = null;
      this.dataDialogForm.shopSpace = null;
    },
    //完成采购计划
    handleOk(index,item) {
      if (item.status == 0 ){
        this.$confirm("该计划已完成","提示",{
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        return;
      }
      this.$confirm("采购计划是否完成，请确认！", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
      .then(() => {
        if (this.dialogFormSubmitVisible) {
          return;
        }
        this.dialogFormSubmitVisible = true;
        this.dataDialogForm.status = 0;
        this.dataDialogForm.id = item.id;
        this.dataDialogForm.isIn = item.isIn;
        this.dataProcess(item);
        this.$http.post("/purchase/add", this.dataDialogForm)
        .then((res) =>{
          if (res.data.data === '0') {
            // 表示数据不能被删除
            this.$message({
              type: "warning",
              message: "该计划已完成!",
            });
          } else {
            this.$message({
              type: "success",
              message: "完成采购计划!",
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
            message: "已取消",
          });
        });
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
            .post("/purchase/delete/?id=" + item.id)
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

    //获取
    getShopNameAll() {

      //查询数据库中的所有商品名称
      this.$http.get("/shop/getShopNameAll").then((res) => {
        // console.log("商品", res)
        this.shopAll = res.data.data

      })
    },

    //获取商品类型列表
    getShopTypeList() {
      this.$http.get("/shop/getShopTypeList").then((res) => {
        // console.log("商品类型", res)
        this.shopTypeList = res.data.data;
      })
    },
    //------------------------------------暂时不用pdfOut
    pdfOut() {
      this.$http.get("/pdf/pdfToPurchase").then((res) => {
        // console.log(res);
      })
    },



    //------------------入库操作按钮--------及其检验----------  putStock   index是对应0  ，item对象
    inStock(index, item) {
      this.itemRowPurchaseInfo = item;
      // console.log("this.itemRowPurchaseInfo",this.itemRowPurchaseInfo)
      this.dialogPutStock = true;
      this.getDepositoryAll();

    },

    //----+++++++查询仓库方法  异步
    async getDepositoryAll() {
      this.$http.get("/depository/listAll").then((res) => {
        // console.log("全部仓库", res.data.data);
        this.DepositoryInfoAll = res.data.data;
        // console.log("仓库", this.DepositoryInfoAll)
      })
    },

    //入库  确认
    submitStock(item) {
      const params = {  //参数转
        params: {
          purchaseList: this.itemRowPurchaseInfo
        }
      }
      if (this.PurchaseName === '') {
        this.$message('请先选择入库的仓库哦！');
        return;
      }
      // console.log("params", params)
      this.$http.post("/purchase/putStock?depositoryName=" + this.PurchaseName, this.itemRowPurchaseInfo).then((res) => {
          console.log("入库返回内容", res)
          if (res.data.code === 200) {
            this.changeStatue();
            this.$message({
              message: '入库成功！',
              type: 'success'
            });
            // this.dialogPutStock = false;
          }

          if (res.data.code !=200) {   //重复入库
            this.$message.error(res.data.message);
          }
          this.PurchaseName='';
          this.dialogPutStock = false;
        }
      )
    },

    //修改商品入库状态
    changeStatue(){
      this.dataDialogForm.isIn = 0;
      this.dataDialogForm.status = null;
      this.dataDialogForm.id = this.itemRowPurchaseInfo.id;
      this.dataProcess();
      this.$http.post("/purchase/add", this.dataDialogForm)
        .then((res) =>{
          if (res.data.data === '0') {
            // 表示数据不能被删除
            // this.$message({
            //   type: "warning",
            //   message: "商品入库状态更改失败!",
            // });
          } else {
            // this.$message({
            //   type: "success",
            //   message: "商品入库状态更改成功!",
            // });
          }
          this.dialogFormSubmitVisible = false;
          // 刷新数据
          this.getDataList();
        });
    },

//---------------退货
    exitShop(index,item){
      //0 完成 1 进行中
      //如果item.status  为0完成 则删除采购信息，生成采购退货信息  生成出库清单
      //如果为1进行中 则删除采购信息，生成采购退货信息，不生产出库清单
      if (item.status == 1 ){
        this.$confirm("该计划未审核完成，直接删除","提示",{
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        return;
      }
      this.dialogExitGoods =true; // 替代
      this.itemRowExitGoods = item;
    },
    //退货确认按钮
    checkExitGoods(remark){
      this.$http.post("/purchase/checkAndExitGoods?remark=" + remark , this.itemRowExitGoods).then((res) =>{
        console.log("退货",res)
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

      this.dialogExitGoods=false;
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
      this.$http.get("/supplier/getNameAndAddress").then((res) => {
        // console.log("name-address",res)
        this.restaurants = res.data.data;
      })
    },
    handleSelect(item) {
      // console.log("item",item);
    },
    handleIconClick(ev) {
      // console.log("ev",ev);
    },

    //查询员工方法
    getAdminAll() {
      this.$http.get("/admin/adminAll").then((res) => {
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
      this.$http.get("/purchase/list", params).then((res) => {
        // console.log(this.name, params);
        // console.log(res);
        //需要通过响应的结果配置
        this.Datalist = res.data.data.list;
        this.totalPage = res.data.data.total;
        this.dataListLoading = false;
      });
    },

  },

  mounted() {
    this.getShopNameAll()
    this.getDataList()
    this.getAdminAll()
    this.getShopTypeList()
    this.loadAll();
  }

}
</script>
<style lang="less" scoped>
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


