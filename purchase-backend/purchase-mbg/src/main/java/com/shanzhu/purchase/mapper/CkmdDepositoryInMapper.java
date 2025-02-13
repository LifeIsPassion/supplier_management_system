package com.shanzhu.purchase.mapper;

import com.shanzhu.purchase.model.CkmdDepositoryIn;
import com.shanzhu.purchase.model.CkmdDepositoryInExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CkmdDepositoryInMapper {
    long countByExample(CkmdDepositoryInExample example);

    int deleteByExample(CkmdDepositoryInExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CkmdDepositoryIn row);

    int insertSelective(CkmdDepositoryIn row);

    List<CkmdDepositoryIn> selectByExample(CkmdDepositoryInExample example);

    CkmdDepositoryIn selectByPrimaryKey(Long id);

    CkmdDepositoryIn selectById(int inId);

    CkmdDepositoryIn selectById1(int id);

    CkmdDepositoryIn selectById2(int id);

    List<CkmdDepositoryIn> selectAll();

    List<CkmdDepositoryIn> selectAllNo();

    int updateByExampleSelective(@Param("row") CkmdDepositoryIn row, @Param("example") CkmdDepositoryInExample example);

    int updateByExample(@Param("row") CkmdDepositoryIn row, @Param("example") CkmdDepositoryInExample example);

    int updateByPrimaryKeySelective(CkmdDepositoryIn row);

    int updateByPrimaryKey(CkmdDepositoryIn row);
}