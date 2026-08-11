package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_plan")
@Schema(description = "服务-护理计划")
public class ServicePlan extends BaseEntity {
    @Schema(description = "planName")    private String planName;
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "startDate")    private LocalDateTime startDate;
    @Schema(description = "endDate")    private LocalDateTime endDate;
    @Schema(description = "content")    private String content;
    @Schema(description = "status")    private Integer status;
}
