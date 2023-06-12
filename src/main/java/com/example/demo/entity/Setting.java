package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class Setting {
    @TableId("id")
    private Long id;
    private Integer level1;
    private Integer level2;
    private Integer level3;
    private Date date;
}
