package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class SoftwareFile {
    @TableId(value = "id")
    private Long id;
    private Long softwareId;
    private Long uploaderId;
    private String fileType;
    private String fileName;
    private Date uploadDate;
    private String fileUrl;
    private String fileStatus;
}
