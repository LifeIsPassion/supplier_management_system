<template>
	<!-- 供应商资料 -->
	<div>
		<el-card class="box-card">
			<div class="mod-role">
				<el-form :inline="true" :model="dataForm" class="demo-form-inline">
					<el-form-item>
						<el-input v-model="dataForm.supplier" clearable placeholder="请输入供应商名称"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button @click='getDataList'>查询</el-button>
						<el-button type="primary" @click="openDialog()">增加</el-button>
					</el-form-item>
				</el-form>
				<el-table :data="dataList" style="width: 100%">
					<el-table-column type="selection"> </el-table-column>
					<el-table-column prop="id" label="ID" width="60">
					</el-table-column>
					<el-table-column prop="supplierName" label="供应商名称" width="120" show-overflow-tooltip>
					</el-table-column>
					<el-table-column prop="head" label="负责人" width="120">
					</el-table-column>
					<el-table-column prop="phone" label="电话" width="120">
					</el-table-column>
					<el-table-column prop="address" label="地址" width="120" show-overflow-tooltip>
					</el-table-column>
					<el-table-column prop="branch" label="银行" width="120" show-overflow-tooltip>
					</el-table-column>
					<el-table-column prop="createDate" label="创建时间" width="180">
						<template slot-scope="scope">
						<span>{{ scope.row.createDate == null ? '' : scope.row.createDate.replace("T", " ") }}</span>
					</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="更新时间" width="180">
						<template slot-scope="scope">
						<span>{{ scope.row.updateTime == null ? '' : scope.row.updateTime.replace("T", " ") }}</span>
					</template>
					</el-table-column>

					<el-table-column label="操作">
						<template slot-scope="scope" width="120">
							<el-button size="mini" type="primary"
								@click="handleEdit(scope.$index, scope.row)">编辑</el-button>
							<el-button size="mini" type="danger"
								@click="handleDelete(scope.$index, scope.row)">删除</el-button>
						</template>
					</el-table-column>

				</el-table>
				<el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
					:current-page="pageIndex" :page-sizes="[5, 7,10 ]" :page-size="pageSize" :total="totalPage"
					layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
				</el-pagination>
			</div>

			<!-- Form -->
			<el-dialog :title="dataDialogForm.id === 0 ? '新增供应商' : '更新供应商'" width="30%" :visible.sync="dialogFormVisible"
				@close="closeDialog()">
				<el-form :model="dataDialogForm" :rules="rules" ref="supplierForm">
					<el-form-item label="供应商名称" label-width="120px" prop="supplierName">
						<el-input v-model="dataDialogForm.supplierName" placeholder="供应商名称" style="width: 300px"></el-input>
					</el-form-item>
					<el-form-item label="负责人" label-width="120px" prop="head">
						<el-input v-model="dataDialogForm.head" placeholder="负责人" style="width: 300px"></el-input>
					</el-form-item>
					<el-form-item label="供应商电话" label-width="120px" prop="phone">
						<el-input v-model="dataDialogForm.phone" placeholder="供应商电话" style="width: 300px"></el-input>
					</el-form-item>
					<el-form-item label="供应商地址" label-width="120px" prop="address">
						<el-input v-model="dataDialogForm.address" placeholder="请输入供应商公司地址" style="width: 300px"></el-input>
					</el-form-item>
					<el-form-item label="银行" label-width="120px" prop="branch">
						<el-input v-model="dataDialogForm.branch" placeholder="银行" style="width: 300px"></el-input>
					</el-form-item>
					<el-form-item label="描述信息" label-width="120px" prop="remark">
						<el-input type="textarea" v-model="dataDialogForm.remark" style="width: 300px"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="handleSubmitFormData('supplierForm')">确 定</el-button>
				</div>
			</el-dialog>


		</el-card>
	</div>
</template>
<script>
export default {
	name: "supplier",
	data() {
		return {
			dataForm: {
				supplier: "",
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
				supplierName: "",
				head: "",
				phone: "",
				address: "",
				branch: "",
				//   branchNo: "",
				remark: "",
			},
			//角色验证这里暂不需 validator: checkCustomerName
			rules: {
				supplierName: [{ required: true, message: "请输入供应商姓名", trigger: "blur" }],
				phone: [{ required: true, message: "请输入电话", trigger: "blur" }],
			},
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

		//添加或者编辑客户 通过id来判断
		handleEdit(index, item) {
			this.dialogFormVisible = true;   // 打开更新的窗口
			// 绑定需要更新的数据  数据是table里的prop
			this.dataDialogForm.id = item.id;
			this.dataDialogForm.supplierName = item.supplierName;
			this.dataDialogForm.head = item.head,
				this.dataDialogForm.phone = item.phone;
			this.dataDialogForm.address = item.address;
			//   this.dataDialogForm.email=item.email;
			//   this.dataDialogForm.fax = item.fax;
			this.dataDialogForm.branch = item.branch;
			//   this.dataDialogForm.branchNo = item.branchNo;
			this.dataDialogForm.remark = item.remark;
			// console.log(this.dataDialogForm);
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

					this.$http.post("/supplier/add", this.dataDialogForm)
						.then((res) => {
							// console.log("添加/更新", res);
							this.dialogFormVisible = false; // 关闭窗口
							// 清空添加数据的表单
							this.dataDialogForm = {
								id: "",
								supplierName: "",
								head: "",
								phone: "",
								address: "",
								//    email: "",
								//  fax: "",
								branch: "",
								//  branchNo: "",
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
						.post("/supplier/delete?id=" + item.id)
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
			this.dataDialogForm.id = 0;  //用于后端判断是否为更新 
			this.dataDialogForm.supplierName = "";
			this.dataDialogForm.head = "",
				this.dataDialogForm.phone = "";
			this.dataDialogForm.address = "";
			//  this.dataDialogForm.email = "";
			//  this.dataDialogForm.fax = "";
			this.dataDialogForm.branch = "";
			//  this.dataDialogForm.branchNo = "";
			this.dataDialogForm.remark = "";
		},
		//关闭窗口
		closeDialog() {
			// 清空添加数据的表单
			this.dataDialogForm = {
				supplierName: "",
				head: "",
				phone: "",
				address: "",
				// email: "",
				// fax: "",
				branch: "",
				// branchNo: "",
				remark: "",
			};
		},




		//----------------------		
		getDataList() {
			if (this.dataListLoading) {
				return;
			}
			this.dataListLoading = true;
			// 声明params 查询的参数
			const params = {
				params: {
					pageNum: this.pageIndex,
					pageSize: this.pageSize,
					keyword: this.dataForm.supplier,
				},
			};
			//后端请求
			this.$http.get("/supplier/list", params).then((res) => {
				// console.log(this.name, res)
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