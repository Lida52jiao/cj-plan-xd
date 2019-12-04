package com.yj.bj.mapper;


import com.yj.bj.entity.BatchEntity;
import com.yj.bj.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface BatchMapper extends MyMapper<BatchEntity> {

    int addNumber(@Param("batch") String batch, @Param("aisleCode") String aisleCode, @Param("executeDate") Long executeDate);
}
