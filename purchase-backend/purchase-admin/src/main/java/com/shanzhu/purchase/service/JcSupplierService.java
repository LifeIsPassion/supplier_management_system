package com.shanzhu.purchase.service;

import com.shanzhu.purchase.model.JcmdSupplier;

import java.util.List;
import java.util.Map;

public interface JcSupplierService {

    /**
     * 添加客户
     */
    int create(JcmdSupplier supplier);

    /**
     * 修改客户信息
     */
    int update(JcmdSupplier supplier);

    /**
     * updateOrAddById 判断
     */
    int updateOrAddById(JcmdSupplier supplier);
    /**
     * 批量删除客户
     */
    int delete(Long id);

    /**
     * 获取所有客户
     */
    List<JcmdSupplier > list();

    /**
     * 根据用户名获取供应商
     */
    List<JcmdSupplier> listSupplier(String keyword, Integer pageNum, Integer pageSize);


    /**
     * 分页获取客户列表
     */
    List<JcmdSupplier> list(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 分页获取未审核供应商列表
     * @return
     */
    List<JcmdSupplier> listNoReview(String keyword, Integer pageNum, Integer pageSize);


    List<Map> getNameAndAddress();

    /**
     * 供应商数据展示
     */
    public List getSupplierToEchart();
}
