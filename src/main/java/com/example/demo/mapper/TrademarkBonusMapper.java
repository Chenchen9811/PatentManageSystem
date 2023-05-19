package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TrademarkBonus;

import java.util.List;

public interface TrademarkBonusMapper extends ExpandBaseMapper<TrademarkBonus> {
    List<TrademarkBonus> findBonusListByTrademarkCode(String trademarkCode);
}
