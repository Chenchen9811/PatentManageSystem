package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Setting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingMapper extends BaseMapper<Setting> {
}
