package com.example.demo.mapper;


import com.example.demo.entity.Bill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BillMapper extends ExpandBaseMapper<Bill> {
}
