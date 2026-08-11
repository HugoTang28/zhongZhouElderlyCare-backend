package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@Schema(description = "部门")
public class SysDept extends BaseEntity {

    @Schema(description = "父部门ID，0为根节点")
    private Long parentId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "状态：0正常 1停用")
    private Integer status;

    @Schema(description = "子部门")
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<>();
}
