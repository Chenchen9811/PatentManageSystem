package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class PatentFile {
    @TableId(value = "id")
    private Long id;
    private Long patentId;
    private Long uploaderId;
    private String fileType;
    private String fileName;
    private Date uploadDate;
    private String fileUrl;
    private String fileStatus;
}
