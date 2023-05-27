package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Software;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SoftwareMapper extends ExpandBaseMapper<Software> {
}
