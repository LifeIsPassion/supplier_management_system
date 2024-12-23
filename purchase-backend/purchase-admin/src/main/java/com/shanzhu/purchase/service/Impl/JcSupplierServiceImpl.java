package com.shanzhu.purchase.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.shanzhu.purchase.mapper.JcmdSupplierMapper;
import com.shanzhu.purchase.model.CkmdDepository;
import com.shanzhu.purchase.model.CkmdDepositoryExample;
import com.shanzhu.purchase.model.JcmdSupplier;
import com.shanzhu.purchase.model.JcmdSupplierExample;
import com.shanzhu.purchase.service.JcSupplierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门  service
 */
@Service
public class JcSupplierServiceImpl implements JcSupplierService {

    @Resource
    private JcmdSupplierMapper supplierMapper;


    @Override
    public int create(JcmdSupplier supplier) {
        supplier.setCreateDate(LocalDateTime.now());
        return supplierMapper.insert(supplier);
    }

    @Override
    public int update(JcmdSupplier supplier) {
        supplier.setUpdateTime(LocalDateTime.now());
        return supplierMapper.updateByPrimaryKeySelective(supplier);
    }

    @Override
    public int updateOrAddById(JcmdSupplier supplier) {
        if (supplier.getId() != null && supplier.getId() != 0) {//更新
            this.update(supplier);
        } else {
            this.create(supplier);
        }
        //先默认删除/修改 成功
        return 1;
    }

    @Override
    public int delete(Long id) {

        return supplierMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<JcmdSupplier> list() {
        return supplierMapper.selectByExample(new JcmdSupplierExample());
    }

    @Override
    public List<JcmdSupplier> listSupplier(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        JcmdSupplierExample example = new JcmdSupplierExample();
        if (!StrUtil.isEmpty(keyword)) {
            example.createCriteria().andHeadLike("%" + keyword + "%");
        }
        return supplierMapper.selectByUsername(example);
    }

    @Override
    public List<JcmdSupplier> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        JcmdSupplierExample example = new JcmdSupplierExample();
        if (!StrUtil.isEmpty(keyword)) {
            example.createCriteria().andSupplierNameLike("%" + keyword + "%");
        }
        return supplierMapper.selectByExample(example);
    }
    @Override
    public List<JcmdSupplier> listNoReview(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        JcmdSupplierExample example = new JcmdSupplierExample();
        if (!StrUtil.isEmpty(keyword)) {
            example.createCriteria().andSupplierNameLike("%" + keyword + "%");
        }
        return supplierMapper.selectByExampleNo(example);
    }


    @Override
    public List getNameAndAddress() {
        List<Map<String, String>> list = new ArrayList<>();
        List<JcmdSupplier> supplierList = supplierMapper.selectByExample(new JcmdSupplierExample());
        if (supplierList != null) {
            for (JcmdSupplier supplier : supplierList) {
                String supplierName = supplier.getSupplierName();
                String address = supplier.getAddress();
                Map<String, String> map = new HashMap<>();
                map.put("value", supplierName);
                map.put("address", address);
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public List getSupplierToEchart(){
        List<Map<String, String>> list = new ArrayList<>();
        List<JcmdSupplier> suppliersList = supplierMapper.selectByExample(new JcmdSupplierExample());
        if (suppliersList != null) {
            for (JcmdSupplier jcmdSupplier : suppliersList) {
                String province = jcmdSupplier.getProvince();
                //String subAddress = address.substring(0, address.indexOf("/"));
                if (province != null && !province.equals("")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", province);
                    map.put("value", String.valueOf(70));
                    list.add(map);
                }
            }
        }
        return list;
    }
}
