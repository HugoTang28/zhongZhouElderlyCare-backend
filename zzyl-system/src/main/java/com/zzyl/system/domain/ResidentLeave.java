package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resident_leave")
@Schema(description = "在住-请假管理")
public class ResidentLeave extends BaseEntity {
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "bedId")    private Long bedId;
    @Schema(description = "startTime")    private LocalDateTime startTime;
    @Schema(description = "endTime")    private LocalDateTime endTime;
    @Schema(description = "reason")    private String reason;
    @Schema(description = "status")    private Integer status;
}
