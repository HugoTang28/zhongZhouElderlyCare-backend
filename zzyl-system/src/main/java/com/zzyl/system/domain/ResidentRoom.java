package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resident_room")
@Schema(description = "在住-房型管理")
public class ResidentRoom extends BaseEntity {
    @Schema(description = "roomNo")    private String roomNo;
    @Schema(description = "typeName")    private String typeName;
    @Schema(description = "floor")    private String floor;
    @Schema(description = "bedCount")    private Integer bedCount;
    @Schema(description = "area")    private BigDecimal area;
    @Schema(description = "status")    private Integer status;
}
