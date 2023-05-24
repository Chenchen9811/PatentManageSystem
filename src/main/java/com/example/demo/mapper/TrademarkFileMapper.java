package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SoftwareFile;
import com.example.demo.entity.TrademarkFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrademarkFileMapper extends BaseMapper<TrademarkFile> {
}
