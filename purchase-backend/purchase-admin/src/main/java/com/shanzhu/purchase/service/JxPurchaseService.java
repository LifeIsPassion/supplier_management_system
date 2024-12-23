package com.shanzhu.purchase.service;

import com.shanzhu.purchase.model.JxmdPurchase;

import java.util.List;
import java.util.Map;

public interface JxPurchaseService {

    /**
     * 添加客户
     */
    int create(JxmdPurchase purchase);

    /**
     * 修改客户信息
     */
    int update( JxmdPurchase purchase);

    /**
     * 批量删除客户
     */
    int delete(Long id);

    /**
     * 获取所有客户
     */
    List<JxmdPurchase > list();

    /**
     *
     * 获取前五采购数量
     */
    List<Map> listFive();

    /**
     *
     * 获取前五供应商数量
     */
    List<Map> listFiveSuppiler();

    /**
     * 分页获取客户列表
     */
    List<JxmdPurchase> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据负责人查订单
     */
    List<JxmdPurchase> listOfHead(String keyword, Integer pageSize, Integer pageNum);

    int addOrUpdate(JxmdPurchase purchase);

    int send(int id);

    /**
     * 采购 入库操作
     * @param
     * @return
     */
    int putStock(String depositoryName,JxmdPurchase purchaseList);

    List getNumberAndSupplierName();

    int checkAndExitGoods(JxmdPurchase purchase);

    int stockOut(JxmdPurchase purchase);
}
