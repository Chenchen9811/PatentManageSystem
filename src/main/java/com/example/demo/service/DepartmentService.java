package com.example.demo.service;


import com.example.demo.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();

    Department findDepartmentById(Long departmentId);

    Department findDepartmentByDepartmentName(String departmentName);

    List<Department> getDepartmentListByIds(List<Long> ids);
}
