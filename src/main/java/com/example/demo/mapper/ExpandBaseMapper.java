package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;


public interface ExpandBaseMapper<T> extends BaseMapper<T> {
    Integer insertBatchSomeColumn(List<T> entityList);
}
