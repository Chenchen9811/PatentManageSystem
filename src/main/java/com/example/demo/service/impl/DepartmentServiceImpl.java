package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Department;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.service.DepartmentService;
import com.sun.org.apache.bcel.internal.generic.LMUL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Department findDepartmentById(Long departmentId) {
        return departmentMapper.selectById(departmentId);
    }

    @Override
    public Department findDepartmentByDepartmentName(String departmentName) {
        return departmentMapper.selectOne(new LambdaQueryWrapper<Department>().eq(Department::getDepartmentName, departmentName));
    }

    @Override
    public List<Department> getDepartmentListByIds(List<Long> ids) {
        return departmentMapper.selectBatchIds(ids);
    }
}
