package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("t_proposal_file")
public class ProposalFile {
    @TableId(value = "id")
    private Long id;
    private Long proposalId;
    private Long uploaderId;
    private String fileName;
    private Date uploadDate;
    private String fileUrl;
    private String fileStatus;
}
