package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resident_bed")
@Schema(description = "在住-床位管理")
public class ResidentBed extends BaseEntity {
    @Schema(description = "bedNo")    private String bedNo;
    @Schema(description = "roomId")    private Long roomId;
    @Schema(description = "status")    private Integer status;
}
