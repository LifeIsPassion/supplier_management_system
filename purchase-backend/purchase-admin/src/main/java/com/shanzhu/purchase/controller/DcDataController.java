package com.shanzhu.purchase.controller;






import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.shanzhu.purchase.mapper.CkmdDepositoryInMapper;
import com.shanzhu.purchase.model.CkmdDepositoryIn;
import com.shanzhu.purchase.model.JxmdPurchase;
import com.shanzhu.purchase.model.JxmdPurchaseExit;
import com.shanzhu.purchase.service.JxPurchaseExitService;
import com.shanzhu.purchase.service.JxPurchaseService;
import com.shanzhu.purchase.service.JxSaleExitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author ChenYuTeng
 * @Date 2024/12/16 23:19
 * @description: 数据导出
 */
@Controller
@RequestMapping("/dcData")
public class DcDataController {

    @Resource
    private JxPurchaseService purchaseService;

    @Resource
    private JxPurchaseExitService jxPurchaseExitService;

    @Resource
    private CkmdDepositoryInMapper ckmdDepositoryInMapper;

    @RequestMapping(value = "/exportPurchase",method = RequestMethod.GET)
    public void export(HttpServletResponse response) throws IOException {
        // 从数据库查询出所有的数据
        List<JxmdPurchase> list = purchaseService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("number", "采购编号");
        writer.addHeaderAlias("purchaseUser", "采购人");
        writer.addHeaderAlias("shop", "采购商品");
        writer.addHeaderAlias("shopType", "商品所属类");
        writer.addHeaderAlias("supplier", "采购供应商");
        writer.addHeaderAlias("quantity", "采购数量");
        writer.addHeaderAlias("time", "创建时间");
        writer.addHeaderAlias("price", "采购价格");
        writer.addHeaderAlias("totalPrice", "总价");
        writer.addHeaderAlias("shopSpace", "商品占用空间");
        writer.addHeaderAlias("specs", "单位规格");
        writer.addHeaderAlias("isIn", "商品是否入库");
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("isDestroy", "是否存在");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officeedocument.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
    @RequestMapping(value = "/exportPurchaseExit",method = RequestMethod.GET)
    public void exportPurchaseExit(HttpServletResponse response) throws IOException{
        // 从数据库查询出所有的数据
        List<JxmdPurchaseExit> list = jxPurchaseExitService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("number", "采购编号");
        writer.addHeaderAlias("exitNumber", "退采编号");
        writer.addHeaderAlias("shop", "采购商品");
        writer.addHeaderAlias("num", "商品所属类");
        writer.addHeaderAlias("supplier", "退采数量");
        writer.addHeaderAlias("price", "单价");
        writer.addHeaderAlias("totalPrice", "总价");
        writer.addHeaderAlias("time", "日期");
        writer.addHeaderAlias("reason", "原因");
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("specs", "单位规格");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officeedocument.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
    @RequestMapping(value = "/exportDeList",method = RequestMethod.GET)
    public void exportDeList(HttpServletResponse response) throws IOException{
        // 从数据库查询出所有的数据
        List<CkmdDepositoryIn> list = ckmdDepositoryInMapper.selectAllNo();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("sourceNumber", "来源");
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("inId", "入库编号");
        writer.addHeaderAlias("depository", "入库仓库名");
        writer.addHeaderAlias("shopName", "入库商名");
        writer.addHeaderAlias("shopPrice", "入库商品单价");
        writer.addHeaderAlias("shopNumber", "入库商品数量");
        writer.addHeaderAlias("priceTotal", "入库商品总价");
        writer.addHeaderAlias("specs", "单位规格");
        writer.addHeaderAlias("date", "入库日期");
        writer.addHeaderAlias("inUser", "入库人");
        writer.addHeaderAlias("shopSupplier", "供应商名");
        writer.addHeaderAlias("isInspection", "质检");
        writer.addHeaderAlias("status", "入库");
        writer.addHeaderAlias("createDate", "创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officeedocument.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

}
