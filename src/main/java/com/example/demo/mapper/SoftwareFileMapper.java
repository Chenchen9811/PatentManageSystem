package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.PatentFile;
import com.example.demo.entity.SoftwareFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SoftwareFileMapper extends BaseMapper<SoftwareFile> {
}
