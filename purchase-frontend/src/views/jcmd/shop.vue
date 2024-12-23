<template>
	<!-- 商品资料 -->
	<el-card class="box-card">
		<div class="mod-role">
			<el-form :inline="true" :model="dataForm" class="demo-form-inline">
				<el-form-item>
					<el-input v-model="dataForm.shop" clearable placeholder="请输入商品名"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button @click='getDataList'>查询</el-button>
					<el-button type="primary" @click="openDialog()">增加</el-button>
				</el-form-item>
			</el-form>

			<el-table ref="multipleTable" :data="dataList" style="width: 100%">
				<el-table-column type="selection"> </el-table-column>
				<el-table-column prop="id" label="ID" width="60">
				</el-table-column>
				<el-table-column prop="name" label="商品名称" width="100" show-overflow-tooltip>
				</el-table-column>
				<el-table-column prop="parentId" label="分类" width="60">
				</el-table-column>
				<el-table-column prop="specs" label="规格" width="60">
				</el-table-column>
				<el-table-column prop="marketPrice" label="市场价格" width="100">
				</el-table-column>
				<el-table-column prop="buildDate" label="创建时间" width="180">
					<template slot-scope="scope">
						<span>{{ scope.row.buildDate == null ? '' : scope.row.buildDate.replace("T", " ") }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="updateDate" label="更新时间" width="180">
					<template slot-scope="scope">
						<span>{{ scope.row.updateDate == null ? '' : scope.row.updateDate.replace("T", " ") }}</span>
					</template>
				</el-table-column>

				<el-table-column label="操作">
					<template slot-scope="scope" width="120">
						<el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
						<el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
					</template>
				</el-table-column>

			</el-table>
			<el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
				:page-sizes="[5,7, 10]" :page-size="pageSize" :total="totalPage"
				layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
			</el-pagination>
		</div>

		<!-- Form -->
		<el-dialog :title="dataDialogForm.id === 0 ? '新增商品' : '更新商品'" width="30%" :visible.sync="dialogFormVisible"
			@close="closeDialog()">
			<el-form :model="dataDialogForm" :rules="rules" ref="customerForm">
				<el-form-item label="商品名称" label-width="120px" prop="name">
					<el-input v-model="dataDialogForm.name" placeholder="商品名称" style="width: 200px"></el-input>
				</el-form-item>

				<el-form-item label="所属类" label-width="120px" prop="parentId">
 					<template>
						<el-select style="width: 200px;" v-model="dataDialogForm.parentId" placeholder="请选择">
							<el-option v-for="item in shopTypeList" :key="item.id" :label="'[' + item.id + '] ' + item.shopType"
								:value="item.id">
							</el-option>
						</el-select>
					</template>
				</el-form-item>
				<el-form-item label="规格" label-width="120px" prop="specs">
            <template>
              <el-select style=" width: 200px;" v-model="dataDialogForm.specs" placeholder="请选择">
                <el-option v-for="item in specsList" :key="item.id" :label="'[' + item.id + '] ' + item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

				<el-form-item label="价格(单位：元)" label-width="120px" prop="marketPrice">
					<el-input v-model="dataDialogForm.marketPrice" placeholder="市场价格" style="width: 200px"></el-input>
				</el-form-item>
				<el-form-item label="描述信息" label-width="120px" prop="remark">
					<el-input type="textarea" v-model="dataDialogForm.remark" style="width: 200px"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="handleSubmitFormData('customerForm')">确 定</el-button>
			</div>
		</el-dialog>



	</el-card>
</template>
<script>
export default {
	name: "shop",
	data() {
		return {

			//商品类型
			shopTypeList: [],

			dataForm: {
				shop: "",
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 5,
			totalPage: 0,
			dataListLoading: false,

			//编辑弹窗框
			dialogFormVisible: false,
			dialogFormSubmitVisible: false,
			dataDialogForm: {
				id: "",
				name: "",
				parentId: "",
				shopNumber: "",
				specs: "",
				marketPrice: "",
				// buildDate: "",
				// updateDate: "",
				remark: "",
			},
			//角色验证 validator: checkCustomerName
			rules: {
				name: [{ required: true, message: "请输入商品名称", trigger: "blur" }],
				//phone: [{ required: true, message: "请输入电话", trigger: "blur" }],

			},

			      //规格列表
		 specsList: [
        { id: 0, name: '斤' }, { id: 1, name: '个' }, { id: 2, name: '件' },
        { id: 3, name: '箱' }
      ],
		};
	},
	methods: {

		sizeChangeHandle(val) {
			this.pageSize = val;
			this.pageIndex = 1;
			this.getDataList();
		},
		currentChangeHandle(val) {
			this.pageIndex = val;
			this.getDataList();
		},

		//编辑
		handleEdit(index, item) {
			this.dialogFormVisible = true;   // 打开更新的窗口
			// 绑定需要更新的数据  数据是table里的prop
			this.dataDialogForm.id = item.id;
			this.dataDialogForm.name = item.name;
			this.dataDialogForm.parentId = item.parentId;
			this.dataDialogForm.shopNumber = item.shopNumber;
			this.dataDialogForm.specs = item.specs;
			this.dataDialogForm.marketPrice = item.marketPrice;
			// this.dataDialogForm.buildDate = item.buildDate;
			// this.dataDialogForm.updateDate = item.updateDate;
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

					this.$http.post("/shop/add", this.dataDialogForm)
						.then((res) => {
							// console.log("添加/修改", res);
							this.dialogFormVisible = false; // 关闭窗口
							// 清空添加数据的表单
							this.dataDialogForm = {
								id: "",
								name: "",
								parentId: "",
								shopNumber: "",
								specs: "",
								marketPrice: "",
								// buildDate: "",
								// updateDate: "",
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
						.post("/shop/delete?id=" + item.id)
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
			this.dataDialogForm.id = 0; //判断是增加还是更新的初始值
			this.dataDialogForm.name = "";
			this.dataDialogForm.parentId = "";
			this.dataDialogForm.shopNumber = "",
				this.dataDialogForm.specs = "";
			this.dataDialogForm.marketPrice = "";
			// this.dataDialogForm.buildDate = "";
			// this.dataDialogForm.updateDate = "";
			this.dataDialogForm.remark = "";
		},
		//关闭窗口
		closeDialog() {
			// 清空添加数据的表单
			this.dataDialogForm = {
				id: "",
				name: "",
				parentId: "",
				shopNumber: "",
				specs: "",
				marketPrice: "",
				// buildDate: "",
				// updateDate: "",
				remark: "",
			};
		},

		getShopTypeList() {
			this.$http.get("/shop/getShopTypeList").then((res) => {
				// console.log("商品类型", res)
				this.shopTypeList = res.data.data;
			})
		},


		//-------------------------------------------
		getDataList() {

			this.getShopTypeList();
			if (this.dataListLoading) {
				return;
			}
			this.dataListLoading = true;
			// 声明params 查询的参数
			const params = {
				params: {
					pageNum: this.pageIndex,
					pageSize: this.pageSize,
					keyword: this.dataForm.shop,
				},
			};
			//后端请求
			this.$http.get("/shop/list", params).then((res) => {
				// console.log(res)
				this.dataList = res.data.data.list;
				this.totalPage = res.data.data.total;
				this.dataListLoading = false;
			});
		}
	},
	mounted() {

		this.getDataList();
	},
};
</script>
