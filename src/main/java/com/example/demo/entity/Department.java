package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Department {
    @TableId(value = "id")
    private Long id;

    private String departmentCode;
    private String departmentName;
    private String departmentPhone;
    private String departmentMail;
    private String departmentNote;
    private String reserve1;
    private String reserve2;
}
