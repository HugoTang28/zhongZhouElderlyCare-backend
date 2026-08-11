package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@Schema(description = "岗位信息")
public class Post extends BaseEntity {

    @Schema(description = "岗位编码")
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "显示顺序")
    private Integer postSort;

    @Schema(description = "状态：0正常 1停用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
