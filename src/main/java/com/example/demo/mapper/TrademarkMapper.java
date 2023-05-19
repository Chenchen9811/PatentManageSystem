package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Trademark;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrademarkMapper extends BaseMapper<Trademark> {
    Trademark findTrademarkByInventorName(String inventorName);
}
