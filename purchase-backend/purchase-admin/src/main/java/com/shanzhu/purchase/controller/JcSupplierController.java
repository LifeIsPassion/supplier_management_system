package com.shanzhu.purchase.controller;


import com.shanzhu.purchase.mapper.JcmdSupplierMapper;
import com.shanzhu.purchase.model.JcmdSupplier;
import com.shanzhu.purchase.service.JcSupplierService;
import com.shanzhu.purchase.util.commonPage;
import com.shanzhu.purchase.util.commonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@Api(tags = "JcSupplierController", description = "jc-4供应商表")
@Tag(name = "JcSupplierController", description = "基础信息管理-供应商表")
@RequestMapping("/supplier")
public class JcSupplierController {

    @Resource
    private JcSupplierService supplierService;

    @Resource
    private JcmdSupplierMapper jcmdSupplierMapper;

    @ApiOperation("添加供应商")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public commonResult create(@RequestBody JcmdSupplier supplier) {
        int count = supplierService.updateOrAddById(supplier);
        if (count > 0) {
            return commonResult.success(count);
        }
        return commonResult.failed();
    }

    @ApiOperation("修改供应商")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public commonResult update(@RequestBody JcmdSupplier supplier) {
        int count = supplierService.updateOrAddById(supplier);
        if (count > 0) {
            return commonResult.success(count);
        }
        return commonResult.failed();
    }

    @ApiOperation("删除客户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public commonResult delete(Long id) {
        int count = supplierService.delete(id);
        if (count > 0) {
            return commonResult.success(count);
        }
        return commonResult.failed();
    }


    @ApiOperation("根据 供应商姓名或 地区分页获取 ")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public commonResult<commonPage<JcmdSupplier>> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<JcmdSupplier> supplierList = supplierService.list(keyword, pageNum, pageSize);
        return commonResult.success(commonPage.restPage(supplierList));
    }

    @ApiOperation("根据负责人获取 ")
    @RequestMapping(value = "/listSupplier", method = RequestMethod.GET)
    @ResponseBody
    public commonResult<commonPage<JcmdSupplier>> listSupplier(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<JcmdSupplier> supplierList = supplierService.listSupplier(keyword, pageNum, pageSize);
        return commonResult.success(commonPage.restPage(supplierList));
    }

    @ApiOperation("根据 供应商姓名或 地区分页获取 ")
    @RequestMapping(value = "/listNoReview", method = RequestMethod.GET)
    @ResponseBody
    public commonResult<commonPage<JcmdSupplier>> listNoReview(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<JcmdSupplier> supplierList = supplierService.listNoReview(keyword, pageNum, pageSize);
        return commonResult.success(commonPage.restPage(supplierList));
    }


    @ApiOperation("获取供应商名和地址")
    @RequestMapping(value = "/getNameAndAddress", method = RequestMethod.GET)
    @ResponseBody
    public commonResult<List> getNameAndAddress() {
        List list = supplierService.getNameAndAddress();
        if (list != null) {
            return commonResult.success(list);
        }
        return commonResult.failed("数据为空");
    }

    @ApiOperation("获取供应商省份")
    @RequestMapping(value = "/getSupplierToEchart",method = RequestMethod.GET)
    @ResponseBody
    public commonResult<List> getSupplierToEChart() {
        List list = supplierService.getSupplierToEchart();
        if (list != null) {
            return commonResult.success(list);
        }
        return commonResult.failed("数据为空");
    }

    @ApiOperation("审核供应商信息")
    @RequestMapping(value = "/reviewData",method = RequestMethod.POST)
    @ResponseBody
    public commonResult reviewData(Long id) {
        int count = jcmdSupplierMapper.updateStatusByPrimaryKey(id);
        if (count > 0) {
            return commonResult.success(count);
        }
        return commonResult.failed();
    }

}
