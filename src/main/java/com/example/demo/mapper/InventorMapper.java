package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Inventor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventorMapper extends BaseMapper<Inventor> {

}
